package com.degreeflow.controller;

import com.degreeflow.model.RequirementGroup;
import com.degreeflow.service.RequirementGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requirementGroups")
public class RequirementGroupController {

    private final RequirementGroupService service;

    public RequirementGroupController(RequirementGroupService service) {
        this.service = service;
    }

    @GetMapping
    public List<RequirementGroup> getGroups(@RequestParam String transcriptId) {
        return service.computeAndReturnGroups(transcriptId);
    }

    @GetMapping("/exists")
    public boolean exists(@RequestParam String transcriptId) {
        return service.doesCourseRequirementExist(transcriptId);
    }

    // ✅ NEW endpoint that runs everything
    @PostMapping("/process")
    public List<RequirementGroup> processTranscript(@RequestParam String transcriptId, @RequestParam String degreeName) {
        service.fetchFromApiAndSave(degreeName, transcriptId); // pulls requirements from API + saves
        return service.computeAndReturnGroups(transcriptId);   // computes and returns updated state
    }
}
