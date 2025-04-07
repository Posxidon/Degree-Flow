package com.degreeflow.controller;

import com.degreeflow.model.TranscriptData;
import com.degreeflow.service.TranscriptParser;
import com.degreeflow.service.TranscriptService;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class TranscriptControllerTest {

    @InjectMocks
    private TranscriptController transcriptController;

    @Mock
    private TranscriptService transcriptService;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUploadTranscript_WithValidPdf_ReturnsParsedData() throws Exception {
        // Step 1: Create a valid PDF in-memory using PDFBox
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        try (PDDocument document = new PDDocument()) {
            document.addPage(new org.apache.pdfbox.pdmodel.PDPage()); // one blank page
            document.save(pdfOutputStream);
        }

        byte[] pdfBytes = pdfOutputStream.toByteArray();
        MockMultipartFile mockFile = new MockMultipartFile("transcript", "sample.pdf", "application/pdf", pdfBytes);
        String studentId = "student123";

        // Step 2: Mock parseTranscript to return dummy data
        TranscriptData mockData = new TranscriptData();
        List<TranscriptData> parsedList = List.of(mockData);

        try (MockedStatic<TranscriptParser> mockParser = mockStatic(TranscriptParser.class)) {
            mockParser.when(() -> TranscriptParser.parseTranscript(any(String.class), eq(studentId)))
                    .thenReturn(parsedList);

            doNothing().when(transcriptService).saveOrUpdateTranscript(parsedList);

            ResponseEntity<List<TranscriptData>> response = transcriptController.uploadTranscript(mockFile, studentId);

            assertEquals(response.getStatusCodeValue(), 200);
            assertNotNull(response.getBody());
            assertEquals(response.getBody().size(), 1);
        }
    }

    @Test
    public void testUploadTranscript_WhenExceptionThrown_Returns500() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
                "transcript", "bad.pdf", "application/pdf", new byte[0]
        );

        ResponseEntity<List<TranscriptData>> response =
                transcriptController.uploadTranscript(mockFile, "studentX");

        assertEquals(response.getStatusCodeValue(), 500);
    }

    @Test
    public void testGetTranscript_WhenFound() {
        String studentId = "s1";
        TranscriptData mockData = new TranscriptData();
        List<TranscriptData> dummy = List.of(mockData);

        when(transcriptService.getAllTranscripts(studentId)).thenReturn(dummy);

        ResponseEntity<List<TranscriptData>> res = transcriptController.getTranscript(studentId);

        assertEquals(res.getStatusCodeValue(), 200);
        assertNotNull(res.getBody());
        assertEquals(res.getBody().size(), 1);
    }

    @Test
    public void testGetTranscript_WhenNotFound() {
        String studentId = "unknown";

        when(transcriptService.getAllTranscripts(studentId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<TranscriptData>> res = transcriptController.getTranscript(studentId);

        assertEquals(res.getStatusCodeValue(), 404);
    }
}
