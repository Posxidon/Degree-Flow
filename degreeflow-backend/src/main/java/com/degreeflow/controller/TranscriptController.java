package com.degreeflow.controller;

import com.degreeflow.model.TranscriptData;
import com.degreeflow.service.TranscriptParser;
import com.degreeflow.service.TranscriptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(
        name = "Transcript Parsing & Upload",
        description = "Upload transcript PDFs, extract data, and retrieve structured results"
)
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/transcripts")
@RequiredArgsConstructor
public class TranscriptController {

    private final TranscriptService transcriptService;

    @Operation(
            summary = "Upload transcript PDF and return extracted data"
    )
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

            // Step 4: Return decrypted transcript data to frontend
            List<TranscriptData> decrypted = transcriptService.getTranscript(studentId);
            return ResponseEntity.ok(decrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Get all transcript entries for a student"
    )
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
