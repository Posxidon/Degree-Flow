package com.degreeflow.service;

import com.degreeflow.model.TranscriptData;
import com.degreeflow.repository.TranscriptRepository;
import com.degreeflow.util.EncryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TranscriptService {

    private static final Logger logger = LoggerFactory.getLogger(TranscriptService.class);

    private final TranscriptRepository transcriptRepository;

    public TranscriptService(TranscriptRepository transcriptRepository) {
        this.transcriptRepository = transcriptRepository;
    }

    /**
     * Encrypts and saves transcript data after deleting previous entries with the same transcriptId.
     */
    @Transactional
    public void saveOrUpdateTranscript(List<TranscriptData> courseRows) {
        if (courseRows == null || courseRows.isEmpty()) {
            logger.warn("No courses to save.");
            return;
        }

        String transcriptId = courseRows.get(0).getTranscriptId();
        logger.info("Saving transcript ID: {}", transcriptId);

        transcriptRepository.deleteAllByTranscriptId(transcriptId);

        for (TranscriptData row : courseRows) {
            logger.debug("Before Encryption - courseCode: {}, grade: {}, term: {}, program: {}",
                    row.getCourseCode(), row.getGrade(), row.getTerm(), row.getProgram());

            if (row.getCourseCode() != null) {
                row.setCourseCode(EncryptionUtil.encrypt(row.getCourseCode()));
            }
            if (row.getGrade() != null) {
                row.setGrade(EncryptionUtil.encrypt(row.getGrade()));
            }
            if (row.getTerm() != null) {
                row.setTerm(EncryptionUtil.encrypt(row.getTerm()));
            }
            if (row.getProgram() != null) {
                row.setProgram(EncryptionUtil.encrypt(row.getProgram()));
            }

            logger.debug("After Encryption - courseCode: {}, grade: {}, term: {}, program: {}",
                    row.getCourseCode(), row.getGrade(), row.getTerm(), row.getProgram());
        }

        transcriptRepository.saveAll(courseRows);
        logger.info("Transcript saved.");
    }

    /**
     * Decrypts transcript data for a given student ID.
     */
    public List<TranscriptData> getTranscript(String studentId) {
        List<TranscriptData> encrypted = transcriptRepository.findAllByStudentId(studentId);

        if (encrypted.isEmpty()) {
            logger.warn("No transcript found for studentId: {}", studentId);
            return encrypted;
        }

        for (TranscriptData row : encrypted) {
            if (row.getCourseCode() != null) {
                row.setCourseCode(EncryptionUtil.decrypt(row.getCourseCode()));
            }
            if (row.getGrade() != null) {
                row.setGrade(EncryptionUtil.decrypt(row.getGrade()));
            }
            if (row.getTerm() != null) {
                row.setTerm(EncryptionUtil.decrypt(row.getTerm()));
            }
            if (row.getProgram() != null) {
                row.setProgram(EncryptionUtil.decrypt(row.getProgram()));
            }
        }

        return encrypted;
    }

    /**
     * Alias for getTranscript.
     */
    public List<TranscriptData> getAllTranscripts(String studentId) {
        return getTranscript(studentId);
    }

    /**
     * Deletes all transcript entries associated with the given transcriptId.
     */
    public void deleteTranscriptById(String transcriptId) {
        logger.info("Deleting transcript with ID: {}", transcriptId);
        transcriptRepository.deleteAllByTranscriptId(transcriptId);
    }
}
