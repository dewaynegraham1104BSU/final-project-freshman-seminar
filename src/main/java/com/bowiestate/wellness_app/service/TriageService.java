package com.bowiestate.wellness_app.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.bowiestate.wellness_app.dto.SymptomTriageRequest;
import com.bowiestate.wellness_app.dto.SymptomTriageResponse;

@Service
public class TriageService {

    public SymptomTriageResponse triage(SymptomTriageRequest request) {
        String raw = normalize(resolveSymptomText(request));
        Integer duration = request == null ? null : request.getDurationHours();

        boolean emergency =
                containsAny(raw, "chest pain", "trouble breathing", "difficulty breathing", "shortness of breath",
                        "can't breathe", "cannot breathe", "fainting", "passed out", "unconscious",
                        "stroke", "slurred speech", "suicidal", "self harm", "self-harm", "overdose",
                        "severe bleeding", "bleeding heavily", "seizure", "severe head injury");

        boolean urgent =
                containsAny(raw, "broken bone", "fracture", "sprain", "severe pain", "painful swelling",
                        "high fever", "fever", "vomiting", "persistent vomiting", "headache", "dizziness",
                        "worse pain", "injury", "burn", "deep cut", "sharp pain", "pain")
                || (duration != null && duration >= 24 && containsAny(raw, "pain", "fever", "nausea", "vomiting"));

        if (emergency) {
            return buildResponse(
                    "EMERGENCY",
                    "Your symptoms suggest an emergency and should be assessed immediately.",
                    List.of(
                            "Call local emergency services now if you are in immediate danger.",
                            "Do not ignore chest pain, breathing trouble, fainting, or severe bleeding.",
                            "If there is trauma or injury, keep the person still and seek urgent care immediately."
                    ),
                    List.of(
                            "Are they breathing normally right now?",
                            "When did the symptoms start?",
                            "Is the pain or bleeding worsening?"
                    )
            );
        }

        if (urgent) {
            return buildResponse(
                    "URGENT",
                    "Your symptoms may need prompt medical evaluation, especially if they are worsening.",
                    List.of(
                            "Schedule an appointment or visit urgent care soon.",
                            "If symptoms worsen or new red flags occur, seek emergency attention.",
                            "Keep a close eye on pain, fever, swelling, or worsening dizziness."
                    ),
                    List.of(
                            "How long have these symptoms been happening?",
                            "Has the pain, fever, or swelling gotten worse?",
                            "Is the injury limiting your movement or breathing?"
                    )
            );
        }

        return buildResponse(
                "SELF_CARE",
                "Your symptoms look manageable with self-care and routine monitoring for now.",
                List.of(
                        "Rest, hydrate, and monitor how you feel over the next 24 hours.",
                        "Use over-the-counter symptom relief if appropriate and safe for you.",
                        "If your symptoms worsen, do not improve, or spread, make a non-urgent appointment."
                ),
                List.of(
                        "How long have the symptoms been going on?",
                        "Have you had any fever, vomiting, or worsening pain?",
                        "Would you like a self-care or campus care recommendation?"
                )
        );
    }

    private SymptomTriageResponse buildResponse(String level, String reasoning, List<String> guidance, List<String> followUp) {
        return new SymptomTriageResponse(level, reasoning, guidance, followUp);
    }

    private String normalize(String text) {
        if (text == null) {
            return "";
        }
        return text.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private String resolveSymptomText(SymptomTriageRequest request) {
        if (request == null) {
            return "";
        }

        StringBuilder text = new StringBuilder();

        for (Method method : request.getClass().getMethods()) {
            if (method.getParameterCount() != 0 || method.getDeclaringClass() == Object.class) {
                continue;
            }

            String methodName = method.getName();
            if (!methodName.startsWith("get") && !methodName.startsWith("is")) {
                continue;
            }

            String property = methodName.startsWith("get")
                    ? methodName.substring(3)
                    : methodName.substring(2);

            if (property.isEmpty() || property.equalsIgnoreCase("Class")) {
                continue;
            }

            String lower = property.substring(0, 1).toLowerCase(Locale.ROOT) + property.substring(1);
            boolean relevant = lower.contains("symptom")
                    || lower.contains("description")
                    || lower.contains("detail")
                    || lower.contains("complaint")
                    || lower.contains("problem")
                    || lower.contains("issue")
                    || lower.contains("message")
                    || lower.contains("note")
                    || lower.contains("text");

            if (!relevant) {
                continue;
            }

            try {
                Object value = method.invoke(request);
                if (value != null) {
                    text.append(value).append(" ");
                }
            } catch (IllegalAccessException | InvocationTargetException ignored) {
                // Ignore unsupported accessors and continue
            }
        }

        return text.toString().trim();
    }

    private boolean containsAny(String text, String... options) {
        for (String opt : options) {
            if (opt != null && !opt.isBlank() && text.contains(opt.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }
}

