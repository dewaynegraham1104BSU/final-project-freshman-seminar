package com.bowiestate.wellness_app.dto;

import jakarta.validation.constraints.NotBlank;

public class AppointmentRequestDTO {
    @NotBlank
    private String serviceType;     // e.g., Primary Care, Mental Health, Wellness Check, Urgent Care
    @NotBlank
    private String providerId;      // demo provider id or name
    @NotBlank
    private String preferredTime;  // e.g., "Today 2:00 PM"
    @NotBlank
    private String patientName;

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getProviderId() { return providerId; }
    public void setProviderId(String providerId) { this.providerId = providerId; }

    public String getPreferredTime() { return preferredTime; }
    public void setPreferredTime(String preferredTime) { this.preferredTime = preferredTime; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }
}
