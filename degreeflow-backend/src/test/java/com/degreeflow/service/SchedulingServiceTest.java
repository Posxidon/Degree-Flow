package com.degreeflow.service;

import com.degreeflow.repository.RatingRepository;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
public class SchedulingServiceTest {
    @Mock
    private SchedulingService schedulingService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getScheduleJsonByUserIdTest (){
        assertNotNull(schedulingService.getLatestScheduleJsonByUserId("1"));
    }
}
