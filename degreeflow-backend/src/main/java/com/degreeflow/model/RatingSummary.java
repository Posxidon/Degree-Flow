package com.degreeflow.model;

import lombok.Data;

@Data
public class RatingSummary {
    private String courseCode;
    private Double averageRating;
    private Integer totalRatings;
    private String difficultyCategory;

    public RatingSummary(String courseCode, Double averageRating, Integer totalRatings) {
        this.courseCode = courseCode;
        this.averageRating = averageRating;
        this.totalRatings = totalRatings;
        this.difficultyCategory = calculateDifficultyCategory(averageRating);
    }

    // Helper method to determine difficulty category based on average stars
    private String calculateDifficultyCategory(Double avgStars) {
        if (avgStars == null || avgStars == 0) return "No ratings yet";
        if (avgStars <= 2.5) return "Easy";
        if (avgStars < 3.5) return "Medium";
        return "Hard";
    }
}