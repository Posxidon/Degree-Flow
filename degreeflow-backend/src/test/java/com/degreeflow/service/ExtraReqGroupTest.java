package com.degreeflow.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Transactional // to allow @Rollback
public class ExtraReqGroupTest {

    @Autowired
    private RequirementGroupService requirementGroupService;

    @Rollback(false)
    @Test
    public void testSaveFromRawData_withMockedJson() throws Exception {
        String path = "src/test/java/com/degreeflow/service/sampleDegree.json";
        String rawJson = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> parsed = mapper.readValue(rawJson, new TypeReference<>() {});

        requirementGroupService.saveFromRawData(parsed, "test_transcript_id");
    }
}
