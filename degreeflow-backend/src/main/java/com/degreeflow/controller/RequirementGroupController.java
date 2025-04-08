package com.degreeflow.controller;

import com.degreeflow.model.DataRequirementGroup;
import com.degreeflow.service.RequirementGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/DataRequirementGroups")
public class RequirementGroupController {

    private final RequirementGroupService service;

    public RequirementGroupController(RequirementGroupService service) {
        this.service = service;
    }

    // Fetch requirement groups for a given transcript
    @GetMapping
    public List<DataRequirementGroup> getGroups(@RequestParam String transcriptId) {
        return service.getRequirementGroupsForTranscript(transcriptId); // <- Add this
    }

    // Process transcript: fetch requirements & return updated groups
    @PostMapping("/process")
    public List<DataRequirementGroup> processTranscript(
        @RequestParam String transcriptId,
        @RequestParam String degreeName
    ) {
        service.fetchFromApiAndSave(degreeName, transcriptId);
        return service.getRequirementGroupsForTranscript(transcriptId);
    }

}
