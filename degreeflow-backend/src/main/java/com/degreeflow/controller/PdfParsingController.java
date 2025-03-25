package com.degreeflow.controller;

import com.degreeflow.model.TranscriptData;
import com.degreeflow.service.TranscriptParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/transcripts")
public class PdfParsingController {

    @Autowired
    private TranscriptParser transcriptParser;

    // Endpoint to upload and parse a PDF file
    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("No file uploaded", HttpStatus.BAD_REQUEST);  // If no file is provided
        }

        // Check if the file is a PDF
        if (!file.getContentType().equals("application/pdf")) {
            return new ResponseEntity<>("File must be a PDF", HttpStatus.BAD_REQUEST);  // Validate file type
        }

        try {
            TranscriptData parsedTranscript = transcriptParser.parseTranscript(file);  // Parse the uploaded PDF
            // Optionally, store parsedTranscript in the database here (e.g., transcriptRepository.save(parsedTranscript))
            return new ResponseEntity<>("PDF parsed successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to parse PDF: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
