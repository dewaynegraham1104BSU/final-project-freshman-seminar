package com.bowiestate.wellness_app.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bowiestate.wellness_app.dto.SymptomTriageRequest;
import com.bowiestate.wellness_app.dto.SymptomTriageResponse;

@Service
public class TriageService {

    public SymptomTriageResponse triage(SymptomTriageRequest request) {
        String raw = resolveSymptomText(request);

        Integer duration = (request == null) ? null : request.getDurationHours();

        // Very small normalization for common shorthand/spelling issues
        raw = raw.replaceAll("gsw", "gunshot wound");
        raw = raw.replaceAll("hemor+rag(e|ing)?", "hemorrhage");
        raw = raw.replaceAll("knicked", "kicked"); // adjust if needed

        boolean hasPassingOut = containsAny(raw, "passing out", "passed out", "faint", "fainted", "unconscious", "syncope");
        boolean hasSevereHeadache = containsAny(raw, "very bad headache", "worst headache", "severe headache", "sudden headache");
        boolean hasChestPainOrTrauma = containsAny(raw, "chest", "chest pain", "gunshot wound", "stab", "knife", "bullet", "wound");
        boolean hasSevereBleeding = containsAny(raw, "severe hemorrhage", "severe bleeding", "profuse bleeding", "cannot stop bleeding", "bleeding");
        boolean mentionsArteryVessel = containsAny(raw, "artery", "vessel", "major vessel");
        boolean difficultBreathing = containsAny(raw, "trouble breathing", "difficulty breathing", "can't breathe", "cannot breathe", "shortness of breath");
        boolean strokeLike = containsAny(raw, "face droop", "slurred speech", "arm weakness", "one side", "stroke");
        boolean prolongedSymptoms = duration != null && duration >= 24;

        // Emergency rules
        if (hasPassingOut && hasSevereHeadache) {
            return emergency(
                "Passing out with severe headache can indicate a life-threatening condition.",
                followUp("Is the person awake/alert now?", "Any confusion, stiff neck, seizures?", "Any recent head injury or blood thinners?")
            );
        }

        if (prolongedSymptoms && (difficultBreathing || strokeLike || hasPassingOut)) {
            return emergency(
                "Symptoms have been ongoing for 24 hours or more and can indicate an emergency.",
                followUp("Are they breathing normally right now?", "When did symptoms start?", "Any chest pain, weakness, or worsening confusion?")
            );
        }

        if (difficultBreathing || strokeLike || hasPassingOut) {
            return emergency(
                "These symptoms can indicate an emergency—especially if the person is currently unwell or worsening.",
                followUp("Are they breathing normally right now?", "When did symptoms start?", "Any chest pain, weakness, or worsening confusion?")
            );
        }

        if (hasChestPainOrTrauma && hasSevereBleeding && mentionsArteryVessel) {
            return emergency(
                "Gunshot/stab trauma with suspected major vessel injury and severe bleeding requires immediate emergency care.",
                followUp("Is bleeding currently controlled with direct pressure?", "Is the person conscious?", "Exact location of injury (chest/abdomen/neck)?")
            );
        }

        if (hasSevereBleeding && mentionsArteryVessel) {
            return emergency(
                "Severe bleeding with possible major vessel injury is an emergency.",
                followUp("Can you apply firm direct pressure?", "Is bleeding soaking through bandages?", "Is the person feeling faint?")
            );
        }

        // Urgent rules (non-emergency but needs fast medical assessment)
        if (hasChestPainOrTrauma && containsAny(raw, "pain", "pressure", "heavy")) {
            return urgent(
                "Chest pain/trauma symptoms can be serious—get medical evaluation soon.",
                followUp("How severe is the pain (0-10)?", "Any shortness of breath or sweating?", "Any known heart/lung conditions?")
            );
        }

        // Default fallback
        return nonUrgent(
            "Based on your description, symptoms may be urgent, but it’s not clearly an emergency from the text alone.",
            followUp("How old is the patient?", "Any fever, vomiting, or worsening symptoms?", "When did this start and is it getting worse?")
        );
    }

    private SymptomTriageResponse emergency(String reasoning, List<String> followUp) {
        return new SymptomTriageResponse(
            "EMERGENCY",
            reasoning,
            List.of(
                "Call your local emergency number now (e.g., 911/999/112).",
                "If bleeding: apply firm direct pressure to the wound and keep pressure on.",
                "Do not let the person move unnecessarily if there’s trauma.",
                "If the person becomes unconscious or stops breathing: start CPR if trained and follow dispatcher instructions."
            ),
            followUp
        );
    }

    private SymptomTriageResponse urgent(String reasoning, List<String> followUp) {
        return new SymptomTriageResponse(
            "URGENT",
            reasoning,
            List.of(
                "Seek medical care today (urgent care / emergency department depending on severity).",
                "If symptoms worsen or new red flags appear, treat as an emergency.",
                "Avoid driving yourself if you feel faint, weak, or dizzy."
            ),
            followUp
        );
    }

    private SymptomTriageResponse nonUrgent(String reasoning, List<String> followUp) {
        return new SymptomTriageResponse(
            "NON_URGENT",
            reasoning,
            List.of(
                "Monitor symptoms closely.",
                "Consider contacting a clinician for advice if symptoms persist or worsen.",
                "Stay hydrated and avoid strenuous activity if you feel unwell."
            ),
            followUp
        );
    }

    private List<String> followUp(String... questions) {
        return List.of(questions);
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

            String lower = property.substring(0, 1).toLowerCase() + property.substring(1);
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
            if (opt != null && !opt.isBlank() && text.contains(opt.toLowerCase())) return true;
        }
        return false;
    }
}
