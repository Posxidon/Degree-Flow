
package com.degreeflow.service;

import com.degreeflow.model.RequirementGroup;
import com.degreeflow.model.TranscriptData;
import com.degreeflow.repository.RequirementGroupRepository;
import com.degreeflow.repository.TranscriptDataRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.junit.jupiter.api.Disabled;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ProgressTrackerTest {

    @Autowired
    private ProgressTracker progressTracker;

    @Autowired
    private TranscriptDataRepository transcriptRepo;

    @Autowired
    private RequirementGroupRepository requirementGroupRepo;
    
    @Disabled("Temporarily turned off while debugging API issues")
    @Test
    public void testUpdateTranscript_marksCoursesAsCheckedAndCountsCompleted() {
        String transcriptId = "student123";

        // Create and save 3 transcript courses
        TranscriptData td1 = new TranscriptData();
        td1.setTranscriptId(transcriptId);
        td1.setCourseCode("COMPSCI1MD3");
        td1.setGrade("B");
        td1.setCheckValue(false);
        td1.setCourseTitle("Intro to Programming");
        td1.setGpa("10");
        td1.setTotalGpa("10");
        td1.setUnits("3.0");
        td1.setTerm("2024W");
        td1.setProgram("Computer Science");
        td1.setCoOp("No");
        td1.setStudentId("s123");
        transcriptRepo.save(td1);

        TranscriptData td2 = new TranscriptData();
        td2.setTranscriptId(transcriptId);
        td2.setCourseCode("COMPSCI1XD3");
        td2.setGrade("C");
        td2.setCheckValue(false);
        td2.setCourseTitle("Software Engineering");
        td2.setGpa("10");
        td2.setTotalGpa("10");
        td2.setUnits("3.0");
        td2.setTerm("2024W");
        td2.setProgram("Computer Science");
        td2.setCoOp("No");
        td2.setStudentId("s123");
        transcriptRepo.save(td2);

        TranscriptData td3 = new TranscriptData();
        td3.setTranscriptId(transcriptId);
        td3.setCourseCode("COMPSCI1JC3");
        td3.setGrade("A");
        td3.setCheckValue(false);
        td3.setCourseTitle("Computer Systems");
        td3.setGpa("10");
        td3.setTotalGpa("10");
        td3.setUnits("3.0");
        td3.setTerm("2024W");
        td3.setProgram("Computer Science");
        td3.setCoOp("No");
        td3.setStudentId("s123");
        transcriptRepo.save(td3);

        // Create matching requirement group
        RequirementGroup group = new RequirementGroup(
                500, 3, transcriptId, "Required", List.of("COMPSCI1MD3", "COMPSCI1XD3")
        );
        requirementGroupRepo.save(group);

        // Run update
        progressTracker.updateTranscript(transcriptId);

        // Fetch results
        List<TranscriptData> updatedCourses = transcriptRepo.findAllByTranscriptId(transcriptId);
        List<RequirementGroup> updatedGroups = requirementGroupRepo.findByTranscriptId(transcriptId);

        // Assert courses marked
        for (TranscriptData course : updatedCourses) {
            if (course.getCourseCode().equals("COMPSCI1MD3") || course.getCourseCode().equals("COMPSCI1XD3")) {
                assertTrue(course.isCheckValue());
            } else {
                assertFalse(course.isCheckValue());
            }
        }

        // Assert 2 courses marked complete
        assertEquals(2, updatedGroups.get(0).getNumCompleted());
    }

}
