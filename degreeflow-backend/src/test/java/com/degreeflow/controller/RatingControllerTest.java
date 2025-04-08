package com.degreeflow.controller;

import com.degreeflow.model.Rating;
import com.degreeflow.model.RatingSummary;
import com.degreeflow.service.RatingService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class RatingControllerTest {

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSubmitRating() {
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", "student@mcmaster.ca");
        payload.put("courseCode", "COMPSCI 3MI3");
        payload.put("stars", 4);

        Rating mockRating = new Rating("student@mcmaster.ca", "COMPSCI 3MI3", 4);
        mockRating.setId(1L);
        mockRating.setSubmittedAt(LocalDateTime.now());

        when(ratingService.submitRating("student@mcmaster.ca", "COMPSCI 3MI3", 4)).thenReturn(mockRating);

        ResponseEntity<?> response = ratingController.submitRating(payload);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), mockRating);
        verify(ratingService).submitRating("student@mcmaster.ca", "COMPSCI 3MI3", 4);
    }

    @Test
    public void testGetRatingSummary() {
        RatingSummary mockSummary = new RatingSummary("COMPSCI 3MI3", 4.2, 10);

        when(ratingService.getRatingSummary("COMPSCI 3MI3")).thenReturn(mockSummary);

        ResponseEntity<RatingSummary> response = ratingController.getRatingSummary("COMPSCI 3MI3");

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), mockSummary);
        assertEquals(response.getBody().getCourseCode(), "COMPSCI 3MI3");
        assertEquals(response.getBody().getAverageRating(), 4.2);
        assertEquals(response.getBody().getTotalRatings(), Integer.valueOf(10));
        assertEquals(response.getBody().getDifficultyCategory(), "Hard");
        verify(ratingService).getRatingSummary("COMPSCI 3MI3");
    }

    @Test
    public void testGetRatingByStudentAndCourse() {
        Rating mockRating = new Rating("student@mcmaster.ca", "COMPSCI 3MI3", 4);
        mockRating.setId(1L);
        mockRating.setSubmittedAt(LocalDateTime.now());

        when(ratingService.getRatingByStudentAndCourse("student@mcmaster.ca", "COMPSCI 3MI3"))
                .thenReturn(Optional.of(mockRating));

        ResponseEntity<?> response = ratingController.getRatingByStudentAndCourse("student@mcmaster.ca", "COMPSCI 3MI3");

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), mockRating);
        verify(ratingService).getRatingByStudentAndCourse("student@mcmaster.ca", "COMPSCI 3MI3");
    }
}