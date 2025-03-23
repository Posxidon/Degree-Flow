package com.degreeflow.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


public class Degree {
    private String degreeName;
    @JsonManagedReference
    private List<LevelGroup> reqCourses;

    public void AddRequirementGroup(LevelGroup lg){
        this.reqCourses.add(lg);
    }

    public List<LevelGroup> getReqCourses() {
        return this.reqCourses;
    }

    public String getName(){
        return this.degreeName;
    }

    // No-argument constructor for testing
    public Degree() {
    }

    // Constructor with parameters
    public Degree(String degreeName, List<LevelGroup> reqCourses) {
        this.degreeName = degreeName;
        this.reqCourses = reqCourses;

    }
}
