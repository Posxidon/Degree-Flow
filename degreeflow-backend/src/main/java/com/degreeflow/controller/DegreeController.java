package com.degreeflow.controller;

import com.degreeflow.model.Degree;
import com.degreeflow.service.PathwayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/degree")
public class DegreeController {
    @Autowired
    private PathwayService pathwayService;

    @GetMapping
    public Degree test() {
        return pathwayService.parseDegreePlan(26811);
    }
}
