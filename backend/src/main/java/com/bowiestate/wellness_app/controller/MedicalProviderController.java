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
@RequestMapping("/api/provider")
public class MedicalProviderController {

    // Demo data store (replace with DB/service later)
    private final List<Map<String, String>> providerNotes = new ArrayList<>();

    @GetMapping("/notes")
    public List<Map<String, String>> getAllNotes() {
        return providerNotes;
    }

    // Add a note (e.g., treatment plan, recommendation, etc.)
    @PostMapping("/notes")
    public Map<String, String> addNote(@RequestBody Map<String, String> payload) {
        // Expected keys (example): patientId, note, createdBy
        providerNotes.add(payload);
        return Map.of("status", "created");
    }

    // Example: provider "resources" endpoint (optional)
    @GetMapping("/resources")
    public List<String> getResources() {
        return List.of(
                "https://www.cdc.gov/",
                "https://www.nimh.nih.gov/"
        );
    }
}
