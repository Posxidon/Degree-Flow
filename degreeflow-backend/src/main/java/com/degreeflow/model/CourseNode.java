package com.degreeflow.model;
import java.util.ArrayList;
import java.util.List;

/**
 * adds additional info to the Course class without causing a merge issue
 */
public class CourseNode {
    private Course course;
    private Integer yearsReq;
    // the nested list is for the purpose of representing relationships
    // each list of string will represent a group in which only one is needed
    private List<List<String>> prereqs;
    private List<String> antireqs;
    private String desc;
    private int unit;

    public int getUnit(){return this.unit;}
    public void setUnit(int unit){this.unit = unit;}

    public long getId(){
        return this.course.getId();
    }

    public String getName() {return this.course.getCourseCode();}

    public List<List<String>> getPrereqs() {
        return this.prereqs;
    }

    public List<String> getAntireqs(){
        return this.antireqs;
    }

    public int getYears(){
        return this.yearsReq;
    }

    public String getDesc(){
        return this.desc;
    }

    public void setYear(int year){ this.yearsReq = year; }

    public String getCourseCode(){
        return this.course.getCourseCode();
    }

    // No-argument constructor for testing
    public CourseNode() {
    }

    // Constructor with parameters
    public CourseNode(Course course, int yearsReq, List<List<String>> prereqs,List<String> antireqs) {
        this.course = course;
        this.yearsReq = yearsReq;
        this.prereqs = prereqs;
        this.desc = course.getDescription();
        this.antireqs = antireqs;
    }
}
