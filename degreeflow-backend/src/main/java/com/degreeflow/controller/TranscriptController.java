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
import java.util.List;
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

        // 🔁 Parse all terms from the full transcript
        List<TranscriptData> transcriptList = TranscriptParser.parseTranscript(pdfText, studentId);
        for (TranscriptData data : transcriptList) {
            transcriptService.saveOrUpdateTranscript(data);
        }

        return ResponseEntity.ok("Transcript uploaded successfully with " + transcriptList.size() + " term(s).");
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<List<TranscriptData>> getTranscript(@PathVariable String studentId) {
        List<TranscriptData> transcriptList = transcriptService.getAllTranscripts(studentId);
        if (transcriptList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transcriptList);
    }
}
