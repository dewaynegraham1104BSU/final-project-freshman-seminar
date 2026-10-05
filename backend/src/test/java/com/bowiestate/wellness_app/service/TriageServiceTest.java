package com.bowiestate.wellness_app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.bowiestate.wellness_app.dto.SymptomTriageRequest;

class TriageServiceTest {

    private final TriageService service = new TriageService();

    @Test
    void shouldFlagBrokenBoneAsUrgentWithGuidance() {
        var result = service.triage(new SymptomTriageRequest("broken bone", 1));

        assertEquals("URGENT", result.getTriageLevel());
        assertNotNull(result.getGuidance());
        assertFalse(result.getGuidance().isEmpty());
        assertNotNull(result.getFollowUpQuestions());
        assertFalse(result.getFollowUpQuestions().isEmpty());
        assertTrue(result.getNextSteps().stream().anyMatch(step -> step.contains("Keep the injured area still")));
        assertTrue(result.getReasoningSummary().contains("possible fracture"));
    }

    @Test
    void shouldFlagShortnessOfBreathAsEmergency() {
        var result = service.triage(new SymptomTriageRequest("shortness of breath and chest pain", 1));

        assertEquals("EMERGENCY", result.getTriageLevel());
        assertFalse(result.getGuidance().isEmpty());
        assertFalse(result.getNextSteps().isEmpty());
        assertFalse(result.getFollowUpQuestions().isEmpty());
    }

    @Test
    void shouldNotTreatNegatedChestPainAsAnEmergency() {
        var result = service.triage(new SymptomTriageRequest("I have a fever but no chest pain", 1));

        assertEquals("URGENT", result.getTriageLevel());
        assertTrue(result.getGuidance().stream().anyMatch(guidance -> guidance.contains("fever")));
        assertFalse(result.getGuidance().stream().anyMatch(guidance -> guidance.contains("life-threatening emergency")));
    }

    @Test
    void shouldGiveNonGenericGuidanceForVomiting() {
        var result = service.triage(new SymptomTriageRequest("I have been vomiting", 2));

        assertEquals("URGENT", result.getTriageLevel());
        assertTrue(result.getGuidance().stream().anyMatch(guidance -> guidance.contains("dehydration")));
        assertTrue(result.getNextSteps().stream().anyMatch(step -> step.contains("small, frequent sips")));
        assertFalse(result.getNextSteps().equals(result.getFollowUpQuestions()));
    }
}
