package com.bowiestate.wellness_app.controller;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/provider")
public class MedicalProviderController {

    // Demo data store (replace with DB/service later)
    private final List<Map<String, Object>> providerNotes = new ArrayList<>();

    @GetMapping("/notes")
    public List<Map<String, Object>> getAllNotes() {
        return providerNotes;
    }

    // Add a note (e.g., treatment plan, recommendation, etc.)
    @PostMapping("/notes")
    public Map<String, Object> addNote(@RequestBody Map<String, Object> payload) {
        if (payload == null || payload.isEmpty()) {
            return Map.of("status", "error", "message", "Request body cannot be empty");
        }

        if (!payload.containsKey("patientId") || payload.get("patientId") == null
                || String.valueOf(payload.get("patientId")).isBlank()
                || !payload.containsKey("note") || !(payload.get("note") instanceof String)
                || ((String) payload.get("note")).isBlank()
                || !payload.containsKey("createdBy") || payload.get("createdBy") == null
                || String.valueOf(payload.get("createdBy")).isBlank()) {
            return Map.of("status", "error", "message",
                    "Missing required fields: patientId, note, createdBy");
        }

        Map<String, Object> note = new HashMap<>(payload);
        note.put("id", UUID.randomUUID().toString());
        note.put("createdAt", Instant.now().toString());
        providerNotes.add(note);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "created");
        response.put("note", note);
        return response;
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
