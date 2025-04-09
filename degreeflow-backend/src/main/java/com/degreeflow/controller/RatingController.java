package com.degreeflow.controller;

import com.degreeflow.model.Rating;
import com.degreeflow.model.RatingSummary;
import com.degreeflow.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Course Ratings",
        description = "Submit and view course ratings"
)
@RestController
@RequestMapping({"/api/ratings"})
@CrossOrigin(
        origins = {"*"}
)
public class RatingController {
    @Autowired
    private RatingService ratingService;

    public RatingController() {
    }

    @Operation(
            summary = "Submit or update a course rating"
    )
    @PostMapping
    public ResponseEntity<?> submitRating(@RequestBody Map<String, Object> payload) {
        try {
            String email = (String)payload.get("email");
            String courseCode = (String)payload.get("courseCode");
            Integer stars = (Integer)payload.get("stars");
            if (email != null && courseCode != null && stars != null) {
                Rating rating = this.ratingService.submitRating(email, courseCode, stars);
                return ResponseEntity.ok(rating);
            } else {
                return ResponseEntity.badRequest().body("Missing required fields: email, courseCode, or stars");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while submitting the rating: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Get rating summary",
            description = "Summary of average rating + total votes"
    )
    @GetMapping({"/summary/{courseCode}"})
    public ResponseEntity<RatingSummary> getRatingSummary(@PathVariable String courseCode) {
        RatingSummary summary = this.ratingService.getRatingSummary(courseCode);
        return ResponseEntity.ok(summary);
    }

    @Operation(
            summary = "Get student rating",
            description = "Get a student’s rating for a course"
    )
    @GetMapping({"/student/{email}/course/{courseCode}"})
    public ResponseEntity<?> getRatingByStudentAndCourse(@PathVariable String email, @PathVariable String courseCode) {
        Optional<Rating> rating = this.ratingService.getRatingByStudentAndCourse(email, courseCode);
        return rating.isPresent() ? ResponseEntity.ok((Rating)rating.get()) : ResponseEntity.ok(Map.of("found", false));
    }
}