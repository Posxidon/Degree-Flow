package com.degreeflow.service;

import com.degreeflow.model.TranscriptData;
import com.degreeflow.repository.TranscriptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TranscriptService {

    private final TranscriptRepository transcriptRepository;

    @Transactional
    public void saveOrUpdateTranscript(TranscriptData newTranscript) {
        Optional<TranscriptData> existingTranscript = transcriptRepository.findByStudentId(newTranscript.getStudentId());

        if (existingTranscript.isPresent()) {
            TranscriptData storedTranscript = existingTranscript.get();
            // Compare terms; for a robust solution, parse and compare dates.
            if (newTranscript.getTerm().compareTo(storedTranscript.getTerm()) > 0) {
                newTranscript.setId(storedTranscript.getId());
                newTranscript.encryptData();
                transcriptRepository.save(newTranscript);
            } else {
                // Optionally, update other fields or ignore if not newer.
                newTranscript.setId(storedTranscript.getId());
                newTranscript.encryptData();
                transcriptRepository.save(newTranscript);
            }
        } else {
            newTranscript.encryptData();
            transcriptRepository.save(newTranscript);
        }
    }

    public Optional<TranscriptData> getTranscript(String studentId) {
        Optional<TranscriptData> transcript = transcriptRepository.findByStudentId(studentId);
        transcript.ifPresent(TranscriptData::decryptData);
        return transcript;
    }
}
