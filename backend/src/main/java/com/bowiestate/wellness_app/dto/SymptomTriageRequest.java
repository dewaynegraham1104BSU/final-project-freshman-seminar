package com.bowiestate.wellness_app.dto;

public class SymptomTriageRequest {
    public String symptom;
    public Integer durationHours; // how long it’s been going on

    public SymptomTriageRequest() {}

    public SymptomTriageRequest(String symptom, Integer durationHours) {
        this.symptom = symptom;
        this.durationHours = durationHours;
    }
    public Integer getDurationHours() { return durationHours; }
    public void setDurationHours(Integer durationHours) { this.durationHours = durationHours; }
}
