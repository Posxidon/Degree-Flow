package com.degreeflow.controller;

import com.degreeflow.model.Degree;
import com.degreeflow.service.PathwayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Degree Plans",
        description = "Manage and fetch degree requirements and saved schedules"
)
@RestController
@CrossOrigin(
        origins = {"*"}
)
@RequestMapping({"/api/degree"})
public class DegreeController {
    @Autowired
    private PathwayService pathwayService;

    public DegreeController() {
    }

    @Operation(
            summary = "Get degree requirements",
            description = "Returns requirements for a given degree code"
    )
    @GetMapping({"/requirement"})
    public ResponseEntity<Degree> getRequirement(@RequestParam("degreeName") String degreeName, @RequestParam("showTech") String showTech) {
        System.out.println("param");
        System.out.println(degreeName);
        System.out.println("showTech");
        System.out.println(showTech);
        boolean showParam = Objects.equals(showTech, "true");
        Degree resp = this.pathwayService.parseDegreePlan(degreeName, showParam);
        return resp != null ? ResponseEntity.ok(resp) : ResponseEntity.notFound().build();
    }

    @Operation(
            summary = "Get all degree names and codes",
            description = "Returns a list of all degrees"
    )
    @GetMapping({"/degreeName"})
    public ResponseEntity<List<List<String>>> getAllDegree() {
        List<List<String>> output = this.pathwayService.printCodes();
        return output == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(output);
    }

    @Operation(
            summary = "Save user schedule",
            description = "Post a user’s schedule to the database"
    )
    @PostMapping({"/addSchedule"})
    public ResponseEntity<?> addSchedule(@RequestBody String schedule, @RequestParam("userid") String userid) {
        System.out.println("schedule");
        System.out.println(schedule);
        return this.pathwayService.addToDB(schedule, userid) ? ResponseEntity.ok("success") : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failed db");
    }
}