package com.bowiestate.wellness_app.dto;

import java.util.List;

public class SymptomTriageResponse {
    private String triageLevel; // EMERGENCY / URGENT / NON_URGENT
    private String reasoningSummary;
    private List<String> guidance;
    private List<String> followUpQuestions;

    public SymptomTriageResponse() {}

    public SymptomTriageResponse(String triageLevel, String reasoningSummary,
                                 List<String> guidance, List<String> followUpQuestions) {
        this.triageLevel = triageLevel;
        this.reasoningSummary = reasoningSummary;
        this.guidance = guidance;
        this.followUpQuestions = followUpQuestions;
    }

    public String getTriageLevel() { return triageLevel; }
    public void setTriageLevel(String triageLevel) { this.triageLevel = triageLevel; }

    public String getReasoningSummary() { return reasoningSummary; }
    public void setReasoningSummary(String reasoningSummary) { this.reasoningSummary = reasoningSummary; }

    public List<String> getGuidance() { return guidance; }
    public void setGuidance(List<String> guidance) { this.guidance = guidance; }

    public List<String> getFollowUpQuestions() { return followUpQuestions; }
    public void setFollowUpQuestions(List<String> followUpQuestions) { this.followUpQuestions = followUpQuestions; }

    public List<String> getNextSteps() { return followUpQuestions; }
    public void setNextSteps(List<String> nextSteps) { this.followUpQuestions = nextSteps; }

    public String getLevel() { return triageLevel; }
    public void setLevel(String level) { this.triageLevel = level; }

    public String getNextStep() { return reasoningSummary; }
    public void setNextStep(String nextStep) { this.reasoningSummary = nextStep; }
}
