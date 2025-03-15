package com.degreeflow.service;

import com.degreeflow.model.Course;
import com.degreeflow.model.CourseGroup;
import com.degreeflow.model.Degree;
import com.degreeflow.model.DegreePlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PathwayService {
    @Autowired
    private DegreeService degreeService;
    public List<Course> getTranscript() {
        return null;
    }
    public DegreePlan parseDegreePlan(int degreeId){
        Course c1 = new Course();
        return null;
    }
    public DegreePlan generateDegreePlan(List<Course> previousCourses, Degree degree, int years,DegreePlan coops){
        DegreePlan degreePlan = degree.GetRequiredCourses();
        DegreePlan scenario = new DegreePlan(years);
        int degreeSpan = degreePlan.GetDegreeSpan();
        for (int i=0;i<degreeSpan;i++) {
            for (CourseGroup courseGroup : degreePlan.GetCourses(i)) {
                CourseGroup tempCourse = new CourseGroup();
                for (Course course : previousCourses){
                    if (!courseGroup.IsInGroup(course)) {
                        tempCourse.Add(course);
                    }
                }
                if (tempCourse.GetCourses().size() > 0) {
                    scenario.AddCourseGroup(i, tempCourse);
                }
            }
            List<CourseGroup> cg = coops.GetCourses(i);
            if (cg.size()>0){
                for (CourseGroup courseGroup : cg){
                    scenario.AddCourseGroup(i,courseGroup);
                }
            }
        }
        return scenario;
    }
}
