package com.degreeflow.service;

import com.degreeflow.model.Rating;
import com.degreeflow.model.RatingSummary;
import com.degreeflow.repository.RatingRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSubmitRating_NewRating() {
        Rating rating = new Rating("student@mcmaster.ca", "COMPSCI 3MI3", 4);

        when(ratingRepository.findByEmailAndCourseCode("student@mcmaster.ca", "COMPSCI 3MI3"))
                .thenReturn(Optional.empty());
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        Rating result = ratingService.submitRating("student@mcmaster.ca", "COMPSCI 3MI3", 4);

        // Assert
        assertEquals(result.getEmail(), "student@mcmaster.ca");
        assertEquals(result.getCourseCode(), "COMPSCI 3MI3");
        assertEquals(result.getStars(), Integer.valueOf(4));
        verify(ratingRepository).save(any(Rating.class));
    }

    @Test
    public void testSubmitRating_UpdateExisting() {
        Rating existingRating = new Rating("student@mcmaster.ca", "COMPSCI 3MI3", 3);

        Rating updatedRating = new Rating("student@mcmaster.ca", "COMPSCI 3MI3", 5);

        when(ratingRepository.findByEmailAndCourseCode("student@mcmaster.ca", "COMPSCI 3MI3"))
                .thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(any(Rating.class))).thenReturn(updatedRating);

        Rating result = ratingService.submitRating("student@mcmaster.ca", "COMPSCI 3MI3", 5);

        // Assert
        assertEquals(result.getStars(), Integer.valueOf(5));
        verify(ratingRepository).save(any(Rating.class));
    }

    @Test
    public void testGetRatingSummary() {
        when(ratingRepository.getAverageRatingForCourse("COMPSCI 3MI3")).thenReturn(4.0);
        when(ratingRepository.countRatingsByCourseCode("COMPSCI 3MI3")).thenReturn(2);

        RatingSummary summary = ratingService.getRatingSummary("COMPSCI 3MI3");

        assertEquals(summary.getCourseCode(), "COMPSCI 3MI3");
        assertEquals(summary.getAverageRating(), 4.0);
        assertEquals(summary.getTotalRatings(), Integer.valueOf(2));
        assertEquals(summary.getDifficultyCategory(), "Hard");

        // Verify repository methods were called
        verify(ratingRepository).getAverageRatingForCourse("COMPSCI 3MI3");
        verify(ratingRepository).countRatingsByCourseCode("COMPSCI 3MI3");
    }

    @Test
    public void testGetRatingByStudentAndCourse() {
        Rating expectedRating = new Rating("student@mcmaster.ca", "COMPSCI 3MI3", 4);
        when(ratingRepository.findByEmailAndCourseCode("student@mcmaster.ca", "COMPSCI 3MI3"))
                .thenReturn(Optional.of(expectedRating));

        Optional<Rating> result = ratingService.getRatingByStudentAndCourse("student@mcmaster.ca", "COMPSCI 3MI3");

        assertTrue(result.isPresent());
        assertEquals(result.get().getEmail(), "student@mcmaster.ca");
        assertEquals(result.get().getCourseCode(), "COMPSCI 3MI3");
        assertEquals(result.get().getStars(), Integer.valueOf(4));

        // Verify repository method was called
        verify(ratingRepository).findByEmailAndCourseCode("student@mcmaster.ca", "COMPSCI 3MI3");
    }
}