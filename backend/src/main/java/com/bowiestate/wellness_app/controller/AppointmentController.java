package com.bowiestate.wellness_app.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bowiestate.wellness_app.model.Appointment;

@RestController
public class AppointmentController {
    /** Request body used when creating an appointment. */
    public static class AppointmentRequest {
        public String patientName;
        public String appointmentType;
        public String preferredTime;
    }

    private final List<Appointment> appointments = Collections.synchronizedList(new ArrayList<>());

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequest request) {
        if (request == null || request.patientName == null || request.appointmentType == null || request.preferredTime == null
                || request.patientName.isBlank() || request.appointmentType.isBlank() || request.preferredTime.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Appointment appt = new Appointment(request.patientName, request.appointmentType, request.preferredTime);
        appointments.add(appt);
        return ResponseEntity.ok(appt);
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAppointments() {
        return ResponseEntity.ok(new ArrayList<>(appointments));
    }
}
