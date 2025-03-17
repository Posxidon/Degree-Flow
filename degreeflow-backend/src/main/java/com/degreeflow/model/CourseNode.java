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

public class CourseNode {
    private Course course;
    private Integer yearsReq;
    @JsonManagedReference
    private List<CourseGroup> prereqs;
    @JsonBackReference
    private List<CourseNode> antireqs;

    public boolean prereqCheck(List<CourseNode> courses){
        for (CourseGroup cg : this.prereqs){
            int numTaken = 0;
            for (CourseNode course : cg.getCourses()){
                boolean cgSatif = false;
                for (CourseNode c :courses){
                    if (Objects.equals(c.getId(), course.getId())){
                        numTaken ++;
                        if (numTaken == cg.getNumReq()){
                            cgSatif = true;
                            break;
                        }
                    }
                }
                if (cgSatif){
                    break;
                }
            }
            if (numTaken < cg.getNumReq()){
                return false;
            }
        }
        return true;
    }

    public long getId(){
        return this.course.getId();
    }

    public List<CourseGroup> getPrereqs() {
        return this.prereqs;
    }

    public List<CourseNode> getAntireqs(){
        return this.antireqs;
    }

    public void addAntireq(CourseNode course){
        this.antireqs.add(course);
    }

    public int getYears(){
        return this.yearsReq;
    }
    public String getCourseCode(){
        return this.course.getCourseCode();
    }

    // No-argument constructor for testing
    public CourseNode() {
    }

    private void addAntireqs_Prereq(List<CourseGroup> cgs){
        System.out.println("adding antireq");
        for (CourseGroup cg : cgs) {
            for (CourseNode c : cg.getCourses()) {
                c.addAntireq(this);
            }
        }
    }
    // Constructor with parameters
    public CourseNode(Course course, int yearsReq, List<CourseGroup> prereqs) {
        this.course = course;
        this.yearsReq = yearsReq;
        this.prereqs = prereqs;
        this.antireqs = new ArrayList<>();
        addAntireqs_Prereq(this.prereqs);
    }
}
