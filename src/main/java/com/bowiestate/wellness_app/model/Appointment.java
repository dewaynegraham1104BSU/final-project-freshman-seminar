package com.bowiestate.wellness_app.model;

import java.time.Instant;
import java.util.UUID;

public class Appointment {
    public String id;
    public String patientName;
    public String appointmentType;
    public String preferredTime;
    public Instant createdAt;

    public Appointment() {}

    public Appointment(String patientName, String appointmentType, String preferredTime) {
        this.id = UUID.randomUUID().toString();
        this.patientName = patientName;
        this.appointmentType = appointmentType;
        this.preferredTime = preferredTime;
        this.createdAt = Instant.now();
    }
}