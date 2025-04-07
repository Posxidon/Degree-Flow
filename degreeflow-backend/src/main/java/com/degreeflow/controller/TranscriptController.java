package com.degreeflow.controller;

import com.degreeflow.model.TranscriptData;
import com.degreeflow.service.TranscriptParser;
import com.degreeflow.service.TranscriptService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/api/transcripts")
@RequiredArgsConstructor
public class TranscriptController {

    private final TranscriptService transcriptService;

    @PostMapping("/upload")
    public ResponseEntity<List<TranscriptData>> uploadTranscript(@RequestParam("transcript") MultipartFile file,
                                                                 @RequestParam("studentId") String studentId) {
        try {
            // Extract raw text from PDF
            String pdfText = extractTextFromPdf(file);

            // Parse the text into TranscriptData
            List<TranscriptData> parsedData = TranscriptParser.parseTranscript(pdfText, studentId);

            // Save using service layer
            transcriptService.saveOrUpdateTranscript(parsedData);

            // Return parsed rows to frontend
            return ResponseEntity.ok(parsedData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<List<TranscriptData>> getTranscript(@PathVariable String studentId) {
        List<TranscriptData> transcriptList = transcriptService.getAllTranscripts(studentId);
        if (transcriptList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transcriptList);
    }

    // Helper method to extract text from PDF file
    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}