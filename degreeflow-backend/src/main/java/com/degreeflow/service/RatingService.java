package com.degreeflow.service;

import com.degreeflow.model.Rating;
import com.degreeflow.model.RatingSummary;
import com.degreeflow.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Transactional
    public Rating submitRating(String email, String courseCode, Integer stars) {
        validateRatingInput(stars);

        Optional<Rating> existingRating = ratingRepository.findByEmailAndCourseCode(email, courseCode);

        if (existingRating.isPresent()) {
            Rating rating = existingRating.get();
            rating.setStars(stars);
            rating.setSubmittedAt(LocalDateTime.now());
            return ratingRepository.save(rating);
        } else {
            Rating newRating = new Rating(email, courseCode, stars);
            return ratingRepository.save(newRating);
        }
    }

    public RatingSummary getRatingSummary(String courseCode) {
        Double averageRating = ratingRepository.getAverageRatingForCourse(courseCode);
        Integer totalRatings = ratingRepository.countRatingsByCourseCode(courseCode);

        // Handle null average in case there are no ratings
        if (averageRating == null) {
            averageRating = 0.0;
        }

        return new RatingSummary(courseCode, averageRating, totalRatings);
    }

    public List<Rating> getRatingsByCourse(String courseCode) {
        return ratingRepository.findByCourseCode(courseCode);
    }

    public List<Rating> getRatingsByStudent(String email) {
        return ratingRepository.findByEmail(email);
    }

    public Optional<Rating> getRatingByStudentAndCourse(String email, String courseCode) {
        return ratingRepository.findByEmailAndCourseCode(email, courseCode);
    }

    @Transactional
    public void deleteRating(Long ratingId) {
        ratingRepository.deleteById(ratingId);
    }

    private void validateRatingInput(Integer stars) {
        if (stars == null || stars < 1 || stars > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5 stars");
        }
    }
}