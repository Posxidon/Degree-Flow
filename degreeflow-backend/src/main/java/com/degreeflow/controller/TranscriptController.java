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
        try {
            System.out.println("==> Received file: " + file.getOriginalFilename());

            PDDocument document = PDDocument.load(file.getInputStream());
            PDFTextStripper pdfStripper = new PDFTextStripper();
            String pdfText = pdfStripper.getText(document);
            System.out.println("==> Extracted text: " + pdfText.substring(0, Math.min(300, pdfText.length())));
            document.close();

            TranscriptData transcriptData = TranscriptParser.parseTranscript(pdfText, studentId);
            System.out.println("==> Parsed data: " + transcriptData);

            transcriptService.saveOrUpdateTranscript(transcriptData);
            return ResponseEntity.ok("Transcript uploaded successfully!");

        } catch (Exception e) {
            e.printStackTrace(); // ← this is the important part
            return ResponseEntity.status(500).body("Error uploading transcript: " + e.getMessage());
        }
    }


    @GetMapping("/{studentId}")
    public ResponseEntity<TranscriptData> getTranscript(@PathVariable String studentId) {
        Optional<TranscriptData> transcript = transcriptService.getTranscript(studentId);
        return transcript.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
