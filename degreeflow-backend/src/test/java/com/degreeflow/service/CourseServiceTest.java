package com.degreeflow.service;

import com.degreeflow.model.Course;
import com.degreeflow.repository.CourseRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Can be left as is or enclosed in try-with-resources if you prefer.
    }

    @Test
    public void testGetAllCourses() {
        // Create Course objects using the no-argument constructor
        Course course1 = new Course();
        course1.setId(1L); // Manually set the id
        course1.setCourseCode("CS101");
        course1.setCourseName("Intro to Computer Science");
        course1.setDescription("Basics of CS");

        Course course2 = new Course();
        course2.setId(2L); // Manually set the id
        course2.setCourseCode("MATH101");
        course2.setCourseName("Calculus");
        course2.setDescription("Intro to Calculus");

        // Create a list of courses
        List<Course> courses = Arrays.asList(course1, course2);

        // Mock the behavior of courseRepository to return the list of courses
        Mockito.when(courseRepository.findAll()).thenReturn(courses);

        // Call the service method and assert the result
        List<Course> result = courseService.getAllCourses();
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getCourseCode(), "CS101"); // .get(0) is fine here
    }
}
