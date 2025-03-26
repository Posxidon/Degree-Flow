package com.degreeflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProtectedController {

    @GetMapping("/protected")
    public ResponseEntity<String> getProtectedResource() {
        return ResponseEntity.ok("Access granted to protected resource via GET.");
    }

    @PostMapping("/protected")
    public ResponseEntity<String> postProtectedResource() {
        return ResponseEntity.ok("Access granted to protected resource via POST.");
    }
}
