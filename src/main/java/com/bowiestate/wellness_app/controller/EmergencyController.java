package com.bowiestate.wellness_app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emergency")
public class EmergencyController {

    public static class EmergencyRequest {
        private String symptomsDescription;

        public EmergencyRequest() {
        }

        public EmergencyRequest(String symptomsDescription) {
            this.symptomsDescription = symptomsDescription;
        }

        public String getSymptomsDescription() {
            return symptomsDescription;
        }

        public void setSymptomsDescription(String symptomsDescription) {
            this.symptomsDescription = symptomsDescription;
        }
    }

    public static class EmergencyGuidanceResponse {
        private String riskLevel;        // "EMERGENCY" | "URGENT" | "NON-EMERGENCY"
        private String immediateAdvice; // what user should do now
        private List<String> nextSteps; // follow-up steps/resources
        private boolean shouldCallNow;  // quick boolean for UI

        public EmergencyGuidanceResponse() {
        }

        public EmergencyGuidanceResponse(String riskLevel, String immediateAdvice, List<String> nextSteps, boolean shouldCallNow) {
            this.riskLevel = riskLevel;
            this.immediateAdvice = immediateAdvice;
            this.nextSteps = nextSteps;
            this.shouldCallNow = shouldCallNow;
        }

        public String getRiskLevel() {
            return riskLevel;
        }

        public void setRiskLevel(String riskLevel) {
            this.riskLevel = riskLevel;
        }

        public String getImmediateAdvice() {
            return immediateAdvice;
        }

        public void setImmediateAdvice(String immediateAdvice) {
            this.immediateAdvice = immediateAdvice;
        }

        public List<String> getNextSteps() {
            return nextSteps;
        }

        public void setNextSteps(List<String> nextSteps) {
            this.nextSteps = nextSteps;
        }

        public boolean isShouldCallNow() {
            return shouldCallNow;
        }

        public void setShouldCallNow(boolean shouldCallNow) {
            this.shouldCallNow = shouldCallNow;
        }
    }

    @GetMapping("/guidance")
    public ResponseEntity<EmergencyGuidanceResponse> guidance() {
        return ResponseEntity.ok(new EmergencyGuidanceResponse(
                "EMERGENCY_GUIDE",
                "If you have severe symptoms (trouble breathing, chest pain, fainting, overdose, or thoughts of self-harm), seek emergency care immediately or call local emergency services/campus emergency resources.",
                List.of(
                        "Call emergency services now if symptoms are severe or worsening.",
                        "If possible, ask someone to stay with you.",
                        "If not in immediate danger, use the symptom checker for triage guidance."
                ),
                true
        ));
    }

    /**
     * Prototype triage endpoint.
     * Later: replace the heuristic with your AI integration endpoint.
     */
    @PostMapping("/triage")
    public ResponseEntity<EmergencyGuidanceResponse> triage(@RequestBody EmergencyRequest request) {

        String symptomText = request == null || request.getSymptomsDescription() == null
                ? ""
                : request.getSymptomsDescription().toLowerCase();

        boolean emergencySignals =
                symptomText.contains("chest pain") ||
                symptomText.contains("shortness of breath") ||
                symptomText.contains("can't breathe") ||
                symptomText.contains("faint") ||
                symptomText.contains("overdose") ||
                symptomText.contains("suicidal") ||
                symptomText.contains("self-harm") ||
                symptomText.contains("unconscious") ||
                symptomText.contains("seizure");

        if (emergencySignals) {
            return ResponseEntity.ok(new EmergencyGuidanceResponse(
                    "EMERGENCY",
                    "Seek emergency care immediately. Call emergency services or campus emergency resources now.",
                    List.of(
                            "Do not wait for an appointment.",
                            "If possible, have someone stay with you.",
                            "Use emergency services if you are alone or symptoms are severe."
                    ),
                    true
            ));
        }

        // Default “not sure / urgent but not emergency”
        return ResponseEntity.ok(new EmergencyGuidanceResponse(
                "UNCERTAIN_URGENT",
                "Your symptoms could be serious. If worsening or you feel unsafe, seek urgent care. Otherwise, use the Symptom Checker for triage guidance.",
                List.of(
                        "Consider campus urgent care if available.",
                        "Monitor symptoms and seek help if they worsen.",
                        "Reach out to mental health resources if you feel overwhelmed."
                ),
                false
        ));
    }
}
