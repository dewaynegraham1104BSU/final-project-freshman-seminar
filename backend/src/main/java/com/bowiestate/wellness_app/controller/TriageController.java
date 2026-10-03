package com.bowiestate.wellness_app.controller;

import java.lang.reflect.InvocationTargetException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bowiestate.wellness_app.dto.SymptomTriageRequest;
import com.bowiestate.wellness_app.dto.SymptomTriageResponse;
import com.bowiestate.wellness_app.service.TriageService;

@RestController
@RequestMapping("/api")

// ✅ ADD THIS: enable CORS for your frontend dev server
@CrossOrigin(
        origins = "http://localhost:5173",
        allowCredentials = "false",
        methods = { org.springframework.web.bind.annotation.RequestMethod.GET,
                    org.springframework.web.bind.annotation.RequestMethod.POST,
                    org.springframework.web.bind.annotation.RequestMethod.OPTIONS },
        allowedHeaders = { "Content-Type", "Authorization" }
)
public class TriageController {

    private final TriageService triageService;

    public TriageController(TriageService triageService) {
        this.triageService = triageService;
    }

    @PostMapping("/triage")
    public ResponseEntity<SymptomTriageResponse> triage(@RequestBody SymptomTriageRequest request) {
        SymptomTriageResponse response = triageService.triage(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/emergency-guidance")
    public ResponseEntity<?> emergencyGuidance(
            @RequestParam String symptom,
            @RequestParam(required = false) Integer durationHours
    ) {
        SymptomTriageRequest req = new SymptomTriageRequest(symptom, durationHours);
        return ResponseEntity.ok(buildEmergencyGuidance(req));
    }

    private String buildEmergencyGuidance(SymptomTriageRequest request) {
        String symptom = readStringProperty(request, "getSymptom", "getSymptoms");
        Integer durationHours = readIntegerProperty(request, "getDurationHours");

        if (symptom.isEmpty()) {
            return "Please provide a symptom to assess emergency guidance.";
        }

        String normalized = symptom.toLowerCase();
        boolean severeWarning = normalized.contains("chest")
                || normalized.contains("difficulty breathing")
                || normalized.contains("severe bleeding")
                || normalized.contains("unconscious")
                || normalized.contains("stroke");

        if (severeWarning || (durationHours != null && durationHours >= 3)) {
            return "Seek emergency care immediately or call emergency services. Do not delay treatment if symptoms are severe or worsening.";
        }

        return "Monitor symptoms closely. If they worsen, persist, or new warning signs appear, seek urgent medical care.";
    }

    private String readStringProperty(Object target, String... methodNames) {
        Object value = readProperty(target, methodNames);
        return value == null ? "" : value.toString().trim();
    }

    private Integer readIntegerProperty(Object target, String... methodNames) {
        Object value = readProperty(target, methodNames);
        if (value == null) return null;

        try {
            return Integer.valueOf(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Object readProperty(Object target, String... methodNames) {
        if (target == null) return null;

        for (String methodName : methodNames) {
            try {
                return target.getClass().getMethod(methodName).invoke(target);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
                // Try the next supported getter name.
            }
        }

        return null;
    }

    // This endpoint is only informative
    @GetMapping("/triage")
    public ResponseEntity<String> triageInfo() {
        return ResponseEntity.ok("Use POST /api/triage");
    }
}
