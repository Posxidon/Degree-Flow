package com.degreeflow.service;

import com.degreeflow.model.TranscriptData;
import com.degreeflow.repository.TranscriptRepository;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class TranscriptServiceTest {

    private TranscriptService transcriptService;
    private TranscriptRepository transcriptRepository;

    private TranscriptData sampleData;

    @BeforeMethod
    public void setUp() {
        transcriptRepository = mock(TranscriptRepository.class);
        transcriptService = new TranscriptService(transcriptRepository);

        sampleData = new TranscriptData();
        sampleData.setStudentId("student123");
        sampleData.setTranscriptId("81f09b15-9151-4226-a0e6-b69df464b895");
        sampleData.setCourseCode("COMP SCI101");
        sampleData.setCourseTitle("Intro to Programming");
        sampleData.setTerm("Fall 2024");
        sampleData.setGrade("A");
        sampleData.setGpa("10.00");
        sampleData.setUnits("3.00");
    }

    @Test
    public void testSaveOrUpdateTranscript_WithValidData() {
        List<TranscriptData> transcriptDataList = Collections.singletonList(sampleData);

        transcriptService.saveOrUpdateTranscript(transcriptDataList);

        verify(transcriptRepository).deleteAllByTranscriptId("81f09b15-9151-4226-a0e6-b69df464b895");
        verify(transcriptRepository).saveAll(transcriptDataList);
    }

    @Test
    public void testSaveOrUpdateTranscript_WithEmptyList() {
        List<TranscriptData> emptyList = Collections.emptyList();

        transcriptService.saveOrUpdateTranscript(emptyList);

        verify(transcriptRepository, never()).deleteAllByTranscriptId(anyString());
        verify(transcriptRepository, never()).saveAll(any());
    }

    @Test
    public void testGetTranscript_ReturnsList() {
        List<TranscriptData> expected = Arrays.asList(sampleData);

        when(transcriptRepository.findAllByStudentId("student123")).thenReturn(expected);

        List<TranscriptData> actual = transcriptService.getTranscript("student123");

        verify(transcriptRepository).findAllByStudentId("student123");
        assert actual.size() == 1;
        assert actual.get(0).getCourseCode().equals("COMP SCI101");
    }

    @Test
    public void testDeleteTranscriptById() {
        String id = UUID.randomUUID().toString();

        transcriptService.deleteTranscriptById(id);

        verify(transcriptRepository).deleteAllByTranscriptId(id);
    }
}
