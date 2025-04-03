package com.degreeflow.service;

import com.degreeflow.model.TranscriptData;
import com.degreeflow.repository.TranscriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TranscriptService {

    private final TranscriptRepository transcriptRepository;

    @Transactional
    public void saveOrUpdateTranscript(TranscriptData newTranscript) {
        // Check if a record already exists for this student and term
        Optional<TranscriptData> existingTranscript =
                transcriptRepository.findByStudentIdAndTerm(newTranscript.getStudentId(), newTranscript.getTerm());

        if (existingTranscript.isPresent()) {
            newTranscript.setId(existingTranscript.get().getId());
            transcriptRepository.save(newTranscript);
        } else {
            transcriptRepository.save(newTranscript);
        }
    }

    public Optional<TranscriptData> getTranscript(String studentId) {
        return transcriptRepository.findByStudentId(studentId);
    }

    public List<TranscriptData> getAllTranscripts(String studentId) {
        return transcriptRepository.findAllByStudentId(studentId);
    }
}
