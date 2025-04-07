package com.degreeflow.service;

import com.degreeflow.model.TranscriptData;
import com.degreeflow.repository.TranscriptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TranscriptService {

    private final TranscriptRepository transcriptRepository;

    public TranscriptService(TranscriptRepository transcriptRepository) {
        this.transcriptRepository = transcriptRepository;
    }

    @Transactional
    public void saveOrUpdateTranscript(List<TranscriptData> courseRows) {
        if (courseRows == null || courseRows.isEmpty()) {
            System.out.println(" No courses to save.");
            return;
        }

        String transcriptId = courseRows.get(0).getTranscriptId();

        transcriptRepository.deleteAllByTranscriptId(transcriptId);
        transcriptRepository.saveAll(courseRows);

        System.out.println(" Transcript saved.");
    }

    public List<TranscriptData> getTranscript(String studentId) {
        return transcriptRepository.findAllByStudentId(studentId);
    }

    public List<TranscriptData> getAllTranscripts(String studentId) {
        return transcriptRepository.findAllByStudentId(studentId);
    }

    public void deleteTranscriptById(String transcriptId) {
        transcriptRepository.deleteAllByTranscriptId(transcriptId);
    }
}
