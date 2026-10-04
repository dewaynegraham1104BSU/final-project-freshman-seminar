package com.bowiestate.wellness_app;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

class AppointmentRequestDemoAppRunner {
    private static final Scanner SCAN = new Scanner(System.in);
    private static final HttpClient HTTP = HttpClient.newHttpClient();
    private static final String BASE_URL = "http://localhost:8080";

    public static class AppointmentRequest {
        private final String patientName;
        private final String appointmentType;
        private final String preferredTime;

        public AppointmentRequest(String patientName, String appointmentType, String preferredTime) {
            this.patientName = patientName;
            this.appointmentType = appointmentType;
            this.preferredTime = preferredTime;
        }

        public String patientName() {
            return patientName;
        }

        public String appointmentType() {
            return appointmentType;
        }

        public String preferredTime() {
            return preferredTime;
        }
    }

    public static void main(String[] args) {
        AppointmentRequestDemoAppRunner app = new AppointmentRequestDemoAppRunner();
        app.runApp();
    }

    public void runApp() {
        while (true) {
            System.out.println();
            System.out.println("Bowie State Wellness App - Prototype (Presentation)");
            System.out.println("A) Symptom Checker (AI Triage - Demo)");
            System.out.println("B) Appointment Scheduling (Demo)");
            System.out.println("C) Emergency Guidance");
            System.out.println("D) Mental Health Resources");
            System.out.println("E) Login & Authentication (Demo)");
            System.out.println("F) Provider Availability & Routing (Demo)");
            System.out.println("Q) Quit");
            System.out.print("Choose an option: ");

            String option = SCAN.nextLine().trim().toUpperCase(Locale.ROOT);
            switch (option) {
                case "A" -> showSymptomTriagePopup();
                case "B" -> showSchedulingPopup();
                case "C" -> showEmergencyPopup();
                case "D" -> showResourcesPopup();
                case "E" -> showLoginPopup();
                case "F" -> showAvailabilityPopup();
                case "Q" -> {
                    System.out.println("Exiting Wellness App demo.");
                    return;
                }
                default -> System.out.println("Invalid option. Please enter A, B, C, D, E, F, or Q.");
            }
        }
    }

    private void showLoginPopup() {
        System.out.println("\n--- E) Login & Authentication (Demo) ---");
        System.out.print("Student/Staff ID or Username: ");
        String username = SCAN.nextLine().trim();
        System.out.print("Password: ");
        String password = SCAN.nextLine().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Please enter username and password.");
            return;
        }

        System.out.println("Login successful (demo). Welcome, " + username + "!");
    }

    private void showSymptomTriagePopup() {
        System.out.println("\n--- A) Symptom Checker (AI Triage - Demo) ---");
        System.out.print("Describe your symptoms: ");
        String symptoms = SCAN.nextLine().trim();

        if (symptoms.isEmpty()) {
            System.out.println("Please describe your symptoms first.");
            return;
        }

        System.out.print("How urgent does it feel? (Not sure/Mild/Moderate/Severe): ");
        String urgency = SCAN.nextLine().trim();
        String text = symptoms.toLowerCase(Locale.ROOT);

        boolean redFlag = text.contains("chest")
                || text.contains("shortness of breath")
                || text.contains("can't breathe")
                || text.contains("severe pain")
                || text.contains("faint")
                || text.contains("suicidal")
                || text.contains("overdose");

        if (redFlag || "SEVERE".equalsIgnoreCase(urgency)) {
            System.out.println("Triage Result (Demo): POSSIBLE URGENT / EMERGENCY.");
            System.out.println("Recommendation: Seek emergency care now (or call appropriate campus/emergency services).");
            System.out.println("If you feel in immediate danger, call emergency services.");
        } else if ("MODERATE".equalsIgnoreCase(urgency)
                || text.contains("fever")
                || text.contains("vomit")
                || text.contains("headache")) {
            System.out.println("Triage Result (Demo): Likely needs timely care.");
            System.out.println("Recommendation: Schedule a campus appointment soon.");
            System.out.println("Providers may decide if higher-level medical care is needed.");
        } else {
            System.out.println("Triage Result (Demo): Likely suitable for self-care and/or non-urgent support.");
            System.out.println("Recommendation: Use resources below and consider a routine appointment if symptoms persist.");
        }
    }

    private void showSchedulingPopup() {
        System.out.println("\n--- B) Appointment Scheduling (Demo) ---");
        System.out.println("Service types: Primary Care, Mental Health, Wellness Check, Urgent Care (if available)");
        System.out.print("Choose service type: ");
        String serviceType = SCAN.nextLine().trim();

        System.out.println("Providers: Available Provider A, Available Provider B, Not Available (Demo)");
        System.out.print("Choose provider: ");
        String provider = SCAN.nextLine().trim();

        if ("Not Available (Demo)".equalsIgnoreCase(provider)) {
            System.out.println("Selected provider is not available. Try another provider or use Routing.");
            return;
        }

        System.out.println("Available times: Today 2:00 PM, Today 4:30 PM, Tomorrow 10:00 AM, Tomorrow 1:00 PM");
        System.out.print("Choose a time slot: ");
        String timeSlot = SCAN.nextLine().trim();

        System.out.print("Your name: ");
        String name = SCAN.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Please enter your name.");
            return;
        }

        System.out.println("Booked! " + name);
        System.out.println(serviceType + " with " + provider);
        System.out.println("Time: " + timeSlot);

        try {
            createAppointment(name, serviceType, timeSlot);
        } catch (Exception e) {
            System.out.println("Unable to sync appointment request to server: " + e.getMessage());
        }
    }

    private void showResourcesPopup() {
        System.out.println("\n--- D) Mental Health Resources ---");
        List<String> resources = new ArrayList<>();
        resources.add("1. Self-help guide: stress reduction & breathing exercises");
        resources.add("2. Coping tools: grounding techniques (5-4-3-2-1)");
        resources.add("3. Crisis planning tips");
        resources.add("4. When to seek urgent help (red flag symptoms)");
        resources.add("5. Mindfulness mini-session (demo)");

        for (String resource : resources) {
            System.out.println(resource);
        }

        System.out.print("Select a resource number to view details (or press Enter to return): ");
        String choice = SCAN.nextLine().trim();
        if (!choice.isEmpty()) {
            int idx;
            try {
                idx = Integer.parseInt(choice);
            } catch (NumberFormatException e) {
                System.out.println("Invalid selection.");
                return;
            }

            if (idx >= 1 && idx <= resources.size()) {
                System.out.println("Resource: " + resources.get(idx - 1));
                System.out.println("(Prototype text—hook this up to your real library later.)");
            } else {
                System.out.println("Selection out of range.");
            }
        }
    }

    private void showAvailabilityPopup() {
        System.out.println("\n--- F) Provider Availability & Routing (Demo) ---");
        System.out.println("Request types: Appointment Request, Mental Health Request, Urgent Triage Request");
        System.out.print("Choose request type: ");
        String type = SCAN.nextLine().trim();

        if ("Mental Health Request".equalsIgnoreCase(type)) {
            System.out.println("Routing Result (Demo):");
            System.out.println("- Best match: Counseling Services");
            System.out.println("- Recommended: Schedule within 24-72 hours");
            System.out.println("- If self-harm risk: use Emergency Guidance");
        } else if ("Urgent Triage Request".equalsIgnoreCase(type)) {
            System.out.println("Routing Result (Demo):");
            System.out.println("- Best match: Urgent Triage + provider review");
            System.out.println("- Recommended: Same-day evaluation (if available)");
            System.out.println("- Otherwise: emergency guidance");
        } else {
            System.out.println("Routing Result (Demo):");
            System.out.println("- Best match: Primary Care / Wellness Check");
            System.out.println("- Recommended: Next available appointment");
        }
    }

    private void showEmergencyPopup() {
        System.out.println("\n--- C) Emergency Guidance ---");
        System.out.println("EMERGENCY GUIDANCE (Prototype)");
        System.out.println("If you have severe symptoms (e.g., trouble breathing, chest pain, fainting, overdose, or thoughts of self-harm):");
        System.out.println("- Seek emergency care immediately.");
        System.out.println("- Call local emergency services or campus emergency resources.");
        System.out.println("- If possible, ask someone to stay with you.");
        System.out.println();
        System.out.println("If symptoms are less severe but still concerning:");
        System.out.println("- Consider campus urgent care (if available).");
        System.out.println("- Use the Symptom Checker for triage guidance.");
    }

    private static void createAppointment(String name, String type, String time) throws Exception {
        AppointmentRequest body = new AppointmentRequest(name, type, time);
        String json = "{\"patientName\":\"" + escapeJson(body.patientName())
            + "\",\"appointmentType\":\"" + escapeJson(body.appointmentType())
            + "\",\"preferredTime\":\"" + escapeJson(body.preferredTime()) + "\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/api/appointments"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            System.out.println("Appointment created successfully.");
            System.out.println("Server response: " + response.body());
        } else {
            System.out.println("Failed to create appointment. Status: " + response.statusCode());
            System.out.println(response.body());
        }
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
