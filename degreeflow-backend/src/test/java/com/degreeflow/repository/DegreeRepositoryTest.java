package com.degreeflow.repository;

import com.degreeflow.model.Course;
import com.degreeflow.model.Degree;
import com.degreeflow.model.JsonSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.testng.Assert.assertTrue;

@DataJpaTest
public class DegreeRepositoryTest {
    @Autowired
    private DegreeRepository degreeRepository;

    @Test
    public void testFindByUserId() {
        String body = "{1:[],2:[]}";
        String id = "test";
        JsonSchedule schedule = new JsonSchedule();
        schedule.setJson(body);
        schedule.setUserId(id);

        degreeRepository.save(schedule);

        Optional<JsonSchedule> retrievedSchedule = degreeRepository.findByUserId(schedule.getUserId());
        assertTrue(retrievedSchedule.isPresent());
    }
}
