package com.degreeflow.controller;

import com.degreeflow.model.TranscriptData;
import com.degreeflow.service.TranscriptParser;
import com.degreeflow.service.TranscriptService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/transcripts")
@RequiredArgsConstructor
public class TranscriptController {

    private final TranscriptService transcriptService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadTranscript(@RequestParam("transcript") MultipartFile file,
                                                   @RequestParam("studentId") String studentId) {
        String pdfText;
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfText = pdfStripper.getText(document);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error parsing PDF: " + e.getMessage());
        }

        // Use the robust TranscriptParser to extract fields.
        TranscriptData transcriptData = TranscriptParser.parseTranscript(pdfText, studentId);
        transcriptService.saveOrUpdateTranscript(transcriptData);
        return ResponseEntity.ok("Transcript uploaded successfully!");
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<TranscriptData> getTranscript(@PathVariable String studentId) {
        Optional<TranscriptData> transcript = transcriptService.getTranscript(studentId);
        return transcript.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
