package com.degreeflow.controller;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CourseFilterControllerTest {

    @InjectMocks
    private CourseFilterController courseFilterController;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetCoursesBySubjectAndLevel() {
        String mockResponse = "\"subjectCode\":\"COMPSCI\",\"catalogNumber\":\"1JC3\"";

        ResponseEntity<?> response = courseFilterController.getCoursesBySubjectAndLevel("COMPSCI", "1");

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(response.getBody().toString().contains(mockResponse));
    }
}