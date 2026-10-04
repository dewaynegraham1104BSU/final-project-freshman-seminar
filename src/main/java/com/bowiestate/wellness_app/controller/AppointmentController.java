package com.bowiestate.wellness_app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/appointments")
public class AppointmentController {
    /** Appointment model used by this controller. */
    public static class Appointment {
        private String patientName;
        private String appointmentType;
        private String preferredTime;

        public Appointment() {
        }

        public Appointment(String patientName, String appointmentType, String preferredTime) {
            this.patientName = patientName;
            this.appointmentType = appointmentType;
            this.preferredTime = preferredTime;
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getAppointmentType() {
            return appointmentType;
        }

        public void setAppointmentType(String appointmentType) {
            this.appointmentType = appointmentType;
        }

        public String getPreferredTime() {
            return preferredTime;
        }

        public void setPreferredTime(String preferredTime) {
            this.preferredTime = preferredTime;
        }
    }

    /** Request body used when creating an appointment. */
    public static class AppointmentRequest {
        private String patientName;
        private String appointmentType;
        private String preferredTime;

        public AppointmentRequest() {
        }

        public String getPatientName() {
            return patientName;
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getAppointmentType() {
            return appointmentType;
        }

        public void setAppointmentType(String appointmentType) {
            this.appointmentType = appointmentType;
        }

        public String getPreferredTime() {
            return preferredTime;
        }

        public void setPreferredTime(String preferredTime) {
            this.preferredTime = preferredTime;
        }

        public boolean hasRequiredFields() {
            return hasText(patientName) && hasText(appointmentType) && hasText(preferredTime);
        }

        private boolean hasText(String value) {
            return value != null && !value.trim().isEmpty();
        }
    }

    private final List<Appointment> appointments = new CopyOnWriteArrayList<>();

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody(required = false) AppointmentRequest request) {
        if (request == null || !request.hasRequiredFields()) {
            return ResponseEntity.badRequest().build();
        }

        Appointment appt = new Appointment(
                request.getPatientName().trim(),
                request.getAppointmentType().trim(),
                request.getPreferredTime().trim());
        appointments.add(appt);
        return ResponseEntity.status(HttpStatus.CREATED).body(appt);
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAppointments() {
        return ResponseEntity.ok(new ArrayList<>(appointments));
    }
}
