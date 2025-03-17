package com.degreeflow.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CourseGroup implements Serializable {

    private List<Course> courseGroup;
    private int numReq;
    private int yearsReq;
    private boolean isSummer;

    public void AddCourse(Course course,boolean isReq){
        this.courseGroup.add(course);
        if (isReq){
            numReq ++;
        }
    }

    public void SetSummer(){
        this.isSummer = true;
    }

    public boolean isSummerCourse(){
        return this.isSummer;
    }

    public List<Integer> GetYears(){
        List<Integer> years = new ArrayList<>();
        for (Course c : this.courseGroup){
            int year = c.getYears();
            if (!years.contains(year)){
                years.add(year);
            }
        }
        return years;
    }
    public List<Course> getCoursesAtYear(int year){
        List<Course> courses = new ArrayList<>();
        for (Course c : this.courseGroup){
            if (c.getYears() == year){
                courses.add(c);
            }
        }
        return courses;
    }

    public int CourseCount(){
        return this.courseGroup.size();
    }

    public boolean isInGroup(Course course){
        for (Course c : this.courseGroup){
            if (Objects.equals(c.getId(), course.getId())){
                return true;
            }
        }
        return false;
    }

    public List<Course> getCourses() {
        return this.courseGroup;
    }

    public int getNumReq(){
        return this.numReq;
    }

    // No-argument constructor for testing
    public CourseGroup() {
    }

    // Constructor with parameters
    public CourseGroup(List<Course> courseGroup,int numReq) {
        this.courseGroup = courseGroup;
        this.numReq = numReq;
        this.isSummer = false;
    }

}

