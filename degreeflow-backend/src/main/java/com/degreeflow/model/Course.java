package com.degreeflow.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Data
public class Course implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String courseCode;
    private String courseName;
    private String description;
    private Integer yearsReq;
    private List<CourseGroup> prereqs;
    private List<Course> antireqs;

    public boolean prereqCheck(List<Course> courses){
        for (CourseGroup cg : this.prereqs){
            int numTaken = 0;
            for (Course course : cg.getCourses()){
                boolean cgSatif = false;
                for (Course c :courses){
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

    public List<CourseGroup> getPrereqs() {
        return this.prereqs;
    }

    public List<Course> getAntireqs(){
        return this.antireqs;
    }

    public void addAntireq(Course course){
        this.antireqs.add(course);
    }

    public int getYears(){
        return this.yearsReq;
    }
    public String getCourseCode(){
        return this.courseCode;
    }

    // No-argument constructor for testing
    public Course() {
    }

    private void addAntireqs_Prereq(List<CourseGroup> cgs){
        for (CourseGroup cg : cgs){
            for (Course c : cg.getCourses()){
                c.addAntireq(this);
            }
        }
    }
    // Constructor with parameters
    public Course(String courseCode, String courseName, String description, int yearsReq, List<CourseGroup> prereqs) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.description = description;
        this.yearsReq = yearsReq;
        this.prereqs = prereqs;
        addAntireqs_Prereq(this.prereqs);
    }
}
