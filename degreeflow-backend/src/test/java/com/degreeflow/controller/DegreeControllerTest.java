package com.degreeflow.controller;

import com.degreeflow.model.Degree;
import com.degreeflow.service.PathwayService;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class DegreeControllerTest {
    @InjectMocks
    private DegreeController degreeController;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRequirement(){
        // testing for valid response
        ResponseEntity<?> response = degreeController.getRequirement("HCOMPSCICO","false");
        assertEquals(response.getStatusCode(),HttpStatus.OK);

        // testing for invalid response
        response = degreeController.getRequirement("","false");
        assertEquals(response.getStatusCode(),HttpStatus.NOT_FOUND);
    }
    @Test
    public void testGetDegree(){
        // testing for valid response
        ResponseEntity<?> response = degreeController.getAllDegree();
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }
    @Test
    public void testAddSchedule(){
        // testing for valid response
        ResponseEntity<?> response = degreeController.addSchedule("","-1");
        assertEquals(response.getStatusCode(),HttpStatus.OK);
    }
}
