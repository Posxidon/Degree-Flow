package com.degreeflow.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CourseGroup {
    @JsonManagedReference
    private static int courseGroupCount;
    private List<CourseNode> courseGroup;
    private int courseGroupId;
    private int numReq;
    private boolean isSummer;
    private String name;

    public void SetSummer(){
        this.isSummer = true;
    }

    public boolean isSummerCourse(){
        return this.isSummer;
    }

    public void setName(String name){
        this.name = name;
    }

    public List<Integer> GetYears(){
        List<Integer> years = new ArrayList<>();
        for (CourseNode c : this.courseGroup){
            int year = c.getYears();
            if (!years.contains(year)){
                years.add(year);
            }
        }
        return years;
    }
    public List<CourseNode> getCoursesAtYear(int year){
        List<CourseNode> courses = new ArrayList<>();
        for (CourseNode c : this.courseGroup){
            if (c.getYears() == year){
                courses.add(c);
            }
        }
        return courses;
    }
    private void assignId(){
        this.courseGroupId = courseGroupCount;
        courseGroupCount ++;
    }
    public int getCourseGroupId(){
        return this.courseGroupId;
    }
    public int CourseCount(){
        return this.courseGroup.size();
    }

    public boolean isInGroup(CourseNode course){
        for (CourseNode c : this.courseGroup){
            if (Objects.equals(c.getId(), course.getId())){
                return true;
            }
        }
        return false;
    }

    public List<CourseNode> getCourses() {
        return this.courseGroup;
    }

    public int getNumReq(){
        return this.numReq;
    }

    // No-argument constructor for testing
    public CourseGroup() {
    }

    // Constructor with parameters
    public CourseGroup(List<CourseNode> courseGroup,int numReq) {
        this.courseGroup = courseGroup;
        this.numReq = numReq;
        this.isSummer = false;
        assignId();
    }


}

