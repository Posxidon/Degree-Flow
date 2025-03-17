package com.degreeflow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


public class Degree implements Serializable {

    private String degreeName;
    private List<CourseGroup> reqCourses;

    public void AddCourse(CourseGroup cg){
        this.reqCourses.add(cg);
    }

    public List<CourseGroup> getReqCourses() {
        return this.reqCourses;
    }

    public String getName(){
        return this.degreeName;
    }

    // No-argument constructor for testing
    public Degree() {
    }

    // Constructor with parameters
    public Degree(String degreeName, List<CourseGroup> reqCourses) {
        this.degreeName = degreeName;
        this.reqCourses = reqCourses;

    }
}
