package com.degreeflow.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class DegreePlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int years;
    @ManyToOne
    @JoinColumn(name = "degree_id")
    private List<List<CourseGroup>> courses;

    private List<List<CourseGroup>> InitCourseList(int years){
        List<List<CourseGroup>> degreePlan = new ArrayList<>();
        for (int i=0;i<years;i++){
            List<CourseGroup> courses = new ArrayList<>();
            degreePlan.add(courses);
        }
        return degreePlan;
    }
    public List<CourseGroup> GetCourses(int years){
        return this.courses.get(years);
    }

    public void AddCourse(int years, Course course){
        List<Course> courseList = new ArrayList<>();
        courseList.add(course);
        CourseGroup courseGroup = new CourseGroup(courseList,1);
        List<CourseGroup> yearCourse = this.courses.get(years);
        yearCourse.add(courseGroup);
    }

    public void AddCourseGroup(int years, CourseGroup courseGroup){
        List<CourseGroup> yearCourse = this.courses.get(years);
        yearCourse.add(courseGroup);
    }

    public int GetDegreeSpan(){
        return this.years;
    }
    public List<List<CourseGroup>> GetDegreePlan(){
        return courses;
    }
    // No-argument constructor for testing
    public DegreePlan() {
    }

    // Constructor with parameters
    public DegreePlan(int years) {
        this.years = years;
        this.courses = InitCourseList(this.years);
    }
}
