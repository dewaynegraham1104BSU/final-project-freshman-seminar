package com.bowiestate.wellness_app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import com.bowiestate.wellness_app.dto.SymptomTriageRequest;

class TriageServiceTest {

    private final TriageService service = new TriageService();

    @Test
    void shouldFlagBrokenBoneAsUrgentWithGuidance() {
        var result = service.triage(new SymptomTriageRequest("broken bone and severe pain", 2));

        assertEquals("URGENT", result.getTriageLevel());
        assertNotNull(result.getGuidance());
        assertFalse(result.getGuidance().isEmpty());
        assertNotNull(result.getFollowUpQuestions());
        assertFalse(result.getFollowUpQuestions().isEmpty());
    }

    @Test
    void shouldFlagShortnessOfBreathAsEmergency() {
        var result = service.triage(new SymptomTriageRequest("shortness of breath and chest pain", 1));

        assertEquals("EMERGENCY", result.getTriageLevel());
        assertFalse(result.getGuidance().isEmpty());
        assertFalse(result.getFollowUpQuestions().isEmpty());
    }
}
