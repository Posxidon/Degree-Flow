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

    /**
     * Endpoint to upload a transcript PDF, parse its contents, and save it.
     * Now returns decrypted data after saving.
     */
    @PostMapping("/upload")
    public ResponseEntity<List<TranscriptData>> uploadTranscript(@RequestParam("transcript") MultipartFile file,
                                                                 @RequestParam("studentId") String studentId) {
        try {
            // Step 1: Extract raw text from PDF
            String pdfText = extractTextFromPdf(file);

            // Step 2: Parse into TranscriptData
            List<TranscriptData> parsedData = TranscriptParser.parseTranscript(pdfText, studentId);

            // Step 3: Save encrypted data
            transcriptService.saveOrUpdateTranscript(parsedData);

            //  Step 4: Return DECRYPTED transcript data to frontend
            List<TranscriptData> decrypted = transcriptService.getTranscript(studentId);
            return ResponseEntity.ok(decrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieve decrypted transcript data for a specific student ID.
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<List<TranscriptData>> getTranscript(@PathVariable String studentId) {
        List<TranscriptData> transcriptList = transcriptService.getAllTranscripts(studentId);
        if (transcriptList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transcriptList);
    }

    /**
     * Helper method to extract plain text from a PDF file.
     */
    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}
