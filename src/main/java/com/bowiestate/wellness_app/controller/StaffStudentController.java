package com.bowiestate.wellness_app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/staff-student/submissions")
public class StaffStudentController {

    // Demo data store (replace with DB/service later)
    private final List<Map<String, String>> submissions = new ArrayList<>();

    @GetMapping
    public List<Map<String, String>> getSubmissions() {
        return submissions;
    }

    // Students/staff submit something (e.g., concerns, requests, forms)
    @PostMapping
    public Map<String, String> addSubmission(@RequestBody Map<String, String> payload) {
        // Expected keys (example): userId, type, message
        submissions.add(payload);
        return Map.of("status", "submitted");
    }
}
