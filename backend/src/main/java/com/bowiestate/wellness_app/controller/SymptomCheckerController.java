package com.bowiestate.wellness_app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/symptom-checker")
public class SymptomCheckerController {

    @PostMapping
    public SymptomCheckerResponse check(@RequestBody SymptomCheckerRequest request) {
        String text = request == null ? "" : (request.symptomsText() == null ? "" : request.symptomsText().toLowerCase());

        String riskLevel;
        if (text.contains("suicide") || text.contains("self-harm") || text.contains("kill myself")) {
            riskLevel = "HIGH";
        } else if (text.contains("panic") || text.contains("anxious") || text.contains("hopeless")) {
            riskLevel = "MEDIUM";
        } else {
            riskLevel = "LOW";
        }

        return new SymptomCheckerResponse(
                riskLevel,
                "This is general guidance. If you feel in immediate danger, use the Emergency Guidance page.",
                List.of(
                        "Try grounding: name 5 things you can see, 4 you can feel, 3 you can hear, 2 you can smell, 1 you can taste.",
                        "Consider reaching out to a trusted person or professional support if symptoms worsen.",
                        "Write down what triggers symptoms so support can be more tailored."
                )
        );
    }

    public record SymptomCheckerRequest(String symptomsText) {}
    public record SymptomCheckerResponse(String riskLevel, String summary, List<String> suggestions) {}
}
