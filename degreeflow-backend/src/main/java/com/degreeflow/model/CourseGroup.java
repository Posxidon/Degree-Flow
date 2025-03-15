package com.degreeflow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Entity
@Data
public class CourseGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private List<Course> courses;
    private int requiredCnt;

    public List<Course> GetCourses(){
        return this.courses;
    }

    public int GetNumRequired(){
        return this.requiredCnt;
    }
    public void Add(Course course){
        this.courses.add(course);
    }
    public boolean IsInGroup(Course course){
        boolean hasCourse = false;
        for (Course c : this.GetCourses()){
            if (Objects.equals(c.getId(), course.getId())){
                hasCourse = true;
                break;
            }
        }
        return hasCourse;
    }

    // No-argument constructor for testing
    public CourseGroup() {
    }

    // Constructor with parameters
    public CourseGroup(List<Course> courses,int numRequired) {
        this.courses = courses;
        this.requiredCnt = numRequired;
    }
}
