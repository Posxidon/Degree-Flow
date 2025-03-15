package com.degreeflow.controller;

import com.degreeflow.model.Degree;
import com.degreeflow.service.DegreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/degrees")
public class DegreeController {
    @Autowired
    private DegreeService degreeService;

    @GetMapping
    public List<Degree> getAllDegrees() {
        return degreeService.getAllDegrees();
    }

    @GetMapping("/{id}")
    public Degree getDegreeById(@PathVariable Long id) {
        return degreeService.getDegreeById(id);
    }

    @PostMapping
    public Degree addDegree(@RequestBody Degree degree) {
        return degreeService.addDegree(degree);
    }

    @DeleteMapping("/{id}")
    public void deleteDegree(@PathVariable Long id) {
        degreeService.deleteDegree(id);
    }
}
