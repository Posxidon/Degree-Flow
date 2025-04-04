package com.degreeflow.service;

import com.degreeflow.model.TranscriptData;
import com.degreeflow.repository.TranscriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TranscriptService {

    private final TranscriptRepository transcriptRepository;

    /**
     * Saves or updates a full transcript consisting of multiple course rows.
     * Each row (course) is uniquely identified by transcriptId + courseCode + term.
     */
    @Transactional
    public void saveOrUpdateTranscript(List<TranscriptData> courseRows) {
        if (courseRows == null || courseRows.isEmpty()) {
            System.out.println(" No courses to save.");
            return;
        }

        String studentId = courseRows.get(0).getStudentId();
        String transcriptId = courseRows.get(0).getTranscriptId();


        // Optional: delete previous transcript data for this transcriptId if updating same one
        transcriptRepository.deleteAllByTranscriptId(transcriptId);

        // Save all new rows (courses) for this transcript
        transcriptRepository.saveAll(courseRows);

        System.out.println(" Transcript saved.");
    }

    public List<TranscriptData> getTranscript(String studentId) {
        return transcriptRepository.findAllByStudentId(studentId);
    }

    public List<TranscriptData> getAllTranscripts(String studentId) {
        return transcriptRepository.findAllByStudentId(studentId);
    }
}
