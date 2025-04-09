package com.degreeflow.controller;

import com.degreeflow.model.TranscriptData;
import com.degreeflow.service.TranscriptParser;
import com.degreeflow.service.TranscriptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(
        origins = {"*"}
)
@Tag(
        name = "Transcript Upload",
        description = "Upload and retrieve transcript data"
)
@RestController
@RequestMapping({"/api/transcripts"})
public class TranscriptController {
    private final TranscriptService transcriptService;

    @Operation(
            summary = "Upload and parse transcript PDF"
    )
    @PostMapping({"/upload"})
    public ResponseEntity<List<TranscriptData>> uploadTranscript(@RequestParam("transcript") MultipartFile file, @RequestParam("studentId") String studentId) {
        try {
            String pdfText = this.extractTextFromPdf(file);
            List<TranscriptData> parsedData = TranscriptParser.parseTranscript(pdfText, studentId);
            this.transcriptService.saveOrUpdateTranscript(parsedData);
            return ResponseEntity.ok(parsedData);
        } catch (Exception var5) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(
            summary = "Get transcript data by student ID"
    )
    @GetMapping({"/{studentId}"})
    public ResponseEntity<List<TranscriptData>> getTranscript(@PathVariable String studentId) {
        List<TranscriptData> transcriptList = this.transcriptService.getAllTranscripts(studentId);
        return transcriptList.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(transcriptList);
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    public TranscriptController(final TranscriptService transcriptService) {
        this.transcriptService = transcriptService;
    }
}