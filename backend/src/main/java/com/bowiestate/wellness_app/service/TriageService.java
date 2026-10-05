package com.bowiestate.wellness_app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.bowiestate.wellness_app.dto.SymptomTriageRequest;
import com.bowiestate.wellness_app.dto.SymptomTriageResponse;

@Service
public class TriageService {

    public SymptomTriageResponse triage(SymptomTriageRequest request) {
        String symptoms = normalize(request == null ? null : request.getSymptom());
        if (symptoms.isBlank()) {
            return buildResponse(
                    "SELF_CARE",
                    "No symptom description was provided, so the checker cannot suggest symptom-specific care.",
                    List.of("Enter a description of what you are experiencing. This checker is not a medical diagnosis."),
                    List.of("Describe your symptoms, when they started, and whether they are getting worse."),
                    List.of()
            );
        }

        boolean breathingProblem = hasAny(symptoms, "trouble breathing", "difficulty breathing",
                "breathing trouble", "shortness of breath", "short of breath", "cannot breathe",
                "can't breathe", "cant breathe", "can't catch my breath", "gasping");
        boolean chestPain = hasAny(symptoms, "chest pain", "chest pressure", "pressure in my chest", "chest hurts");
        boolean strokeSigns = hasAny(symptoms, "stroke", "face drooping", "slurred speech",
                "sudden weakness", "sudden numbness", "one sided weakness", "one sided numbness");
        boolean lossOfConsciousness = hasAny(symptoms, "unconscious", "passed out", "fainted",
                "fainting", "not waking up");
        boolean severeBleeding = hasAny(symptoms, "severe bleeding", "bleeding heavily",
                "wont stop bleeding", "won't stop bleeding", "spurting blood");
        boolean seizure = hasAny(symptoms, "seizure", "convulsion");
        boolean overdose = hasAny(symptoms, "overdose", "poisoning", "poisoned");
        boolean suicidalRisk = hasAny(symptoms, "suicidal", "suicide", "kill myself",
                "hurt myself", "self harm", "self-harm");
        boolean severeAllergicReaction = hasAny(symptoms, "throat swelling", "swollen throat",
                "tongue swelling", "swollen tongue", "anaphylaxis");
        boolean severeHeadache = hasAny(symptoms, "worst headache", "sudden severe headache");
        boolean seriousInjury = hasAny(symptoms, "bone through skin", "bone sticking out", "open fracture",
                "severe head injury", "head injury with confusion");

        if (breathingProblem || chestPain || strokeSigns || lossOfConsciousness || severeBleeding
                || seizure || overdose || suicidalRisk || severeAllergicReaction || severeHeadache || seriousInjury) {
            return emergencyResponse(breathingProblem, chestPain, strokeSigns, lossOfConsciousness,
                    severeBleeding, seizure, overdose, suicidalRisk, severeAllergicReaction,
                    severeHeadache, seriousInjury);
        }

        boolean fracture = hasAny(symptoms, "broken bone", "fracture", "suspected fracture");
        boolean injury = hasAny(symptoms, "sprain", "injury", "injured", "burn", "deep cut", "cut");
        boolean fever = hasAny(symptoms, "fever", "high temperature");
        boolean vomiting = hasAny(symptoms, "vomiting", "throwing up", "cannot keep fluids down");
        boolean dizziness = hasAny(symptoms, "dizziness", "dizzy", "vertigo");
        boolean severeOrWorseningPain = hasAny(symptoms, "severe pain", "worsening pain",
                "sharp pain", "painful swelling", "severe stomach pain");
        boolean pain = hasAny(symptoms, "pain", "ache", "aching");
        boolean headache = hasAny(symptoms, "headache", "migraine");
        boolean nausea = hasAny(symptoms, "nausea", "nauseous");

        boolean urgent = fracture || injury || fever || vomiting || dizziness
                || severeOrWorseningPain || pain || headache || nausea;
        List<String> guidance = new ArrayList<>();
        List<String> nextSteps = new ArrayList<>();
        List<String> followUp = new ArrayList<>();

        if (fracture) {
            guidance.add("A suspected broken bone needs an in-person medical assessment; this description cannot confirm a fracture.");
            nextSteps.add("Keep the injured area still in the position you found it. Do not try to straighten it or push a bone back into place.");
            nextSteps.add("Get urgent medical care today. Call emergency services if the bone is exposed, bleeding is severe, or the limb becomes numb, pale, or cold.");
            followUp.add("When did the injury happen, and where is the suspected fracture?");
        }
        if (injury && !fracture) {
            guidance.add("The injury you described may need examination, especially if the wound is deep, the burn is large, or movement is difficult.");
            nextSteps.add("Protect the injured area and arrange prompt medical evaluation if it is deep, worsening, or limiting movement.");
            followUp.add("How did the injury happen, and is the affected area getting worse?");
        }
        if (fever) {
            guidance.add("A fever can have many causes; its severity, duration, and other symptoms affect what care is appropriate.");
            nextSteps.add("Rest, drink fluids if you can, and contact a clinician promptly if the fever is high, persists, or you feel significantly worse.");
            followUp.add("How long have you had the fever, and have you measured your temperature?");
        }
        if (vomiting) {
            guidance.add("Vomiting can lead to dehydration, particularly if you cannot keep liquids down.");
            nextSteps.add("Take small, frequent sips of fluid if you can. Seek prompt medical care if vomiting continues, you cannot keep fluids down, or you notice signs of dehydration or blood.");
            followUp.add("How long have you been vomiting, and can you keep fluids down?");
        }
        if (dizziness) {
            guidance.add("Dizziness can have different causes; sudden or worsening symptoms should be assessed by a clinician.");
            nextSteps.add("Sit or lie down to reduce the risk of falling, and arrange prompt medical care if the dizziness persists or worsens.");
            followUp.add("Did the dizziness start suddenly, and are you having trouble walking or staying alert?");
        }
        if (pain || severeOrWorseningPain || headache) {
            guidance.add("The pain you described cannot be diagnosed from text. Severe, worsening, unusual, or persistent pain should be assessed promptly.");
            nextSteps.add("Avoid activities that worsen the pain and arrange medical evaluation, especially if it is severe, worsening, or persistent.");
            followUp.add("Where is the pain, when did it begin, and is it getting worse?");
        }
        if (nausea && !vomiting) {
            guidance.add("Nausea has many possible causes; monitor for vomiting, dehydration, or worsening symptoms.");
            nextSteps.add("Try small sips of fluid if tolerated and contact a clinician if nausea persists or worsens.");
            followUp.add("How long have you felt nauseated, and can you keep fluids down?");
        }

        if (!urgent) {
            guidance.add("The description does not identify an emergency warning sign, but a text checker cannot determine the cause or rule out a serious condition.");
            nextSteps.add("Monitor your symptoms and arrange a routine appointment if they persist, return, or interfere with daily activities.");
            nextSteps.add("Seek urgent care if symptoms become severe or worsen; call emergency services for trouble breathing, severe chest pain, fainting, or other life-threatening symptoms.");
            followUp.add("When did the symptoms start, and are they improving or getting worse?");
        }

        String level = urgent ? "URGENT" : "SELF_CARE";
        String summary = urgent
                ? "Your description mentions " + describeSymptoms(fracture, injury, fever, vomiting, dizziness,
                        pain || severeOrWorseningPain, headache, nausea)
                        + ". Prompt medical evaluation is recommended; seek emergency care for severe or rapidly worsening symptoms."
                : "No specific emergency warning sign was recognized in your description. This does not rule out a medical problem.";

        return buildResponse(level, summary, guidance, nextSteps, followUp);
    }

    private SymptomTriageResponse emergencyResponse(boolean breathingProblem, boolean chestPain,
            boolean strokeSigns, boolean lossOfConsciousness, boolean severeBleeding,
            boolean seizure, boolean overdose, boolean suicidalRisk, boolean severeAllergicReaction,
            boolean severeHeadache, boolean seriousInjury) {
        List<String> guidance = new ArrayList<>();
        List<String> nextSteps = new ArrayList<>();
        List<String> followUp = new ArrayList<>();

        guidance.add("The symptoms described may indicate a life-threatening emergency. A text checker cannot assess you safely.");
        if (breathingProblem) guidance.add("Breathing difficulty needs immediate emergency assessment.");
        if (chestPain) guidance.add("New or severe chest pain or pressure can be an emergency.");
        if (strokeSigns) guidance.add("Sudden face, speech, or one-sided strength changes can be signs of a stroke.");
        if (lossOfConsciousness) guidance.add("Loss of consciousness or not waking up needs immediate emergency help.");
        if (severeBleeding) guidance.add("Severe or uncontrolled bleeding needs immediate emergency treatment.");
        if (seizure) guidance.add("A seizure may need emergency care, especially if it is ongoing, repeated, or the person is injured.");
        if (overdose) guidance.add("A possible overdose or poisoning needs immediate expert help, even if the person feels okay.");
        if (suicidalRisk) guidance.add("If you may harm yourself or cannot stay safe, get immediate crisis support and do not stay alone.");
        if (severeAllergicReaction) guidance.add("Swelling of the throat or tongue may block breathing and needs emergency care.");
        if (severeHeadache) guidance.add("A sudden, unusually severe headache can be an emergency.");
        if (seriousInjury) guidance.add("The described injury needs immediate in-person emergency assessment.");

        nextSteps.add("Call your local emergency number now or have someone nearby call. Do not drive yourself.");
        nextSteps.add("Stay with another person and follow the emergency dispatcher's instructions.");
        if (overdose) nextSteps.add("Contact your local poison control center as well; do not induce vomiting unless a professional tells you to.");
        if (suicidalRisk) nextSteps.add("In the U.S. or Canada, call or text 988 for crisis support; call emergency services if danger is immediate.");
        followUp.add("Tell emergency responders when the symptoms began and any relevant medications, substances, or injuries.");
        return buildResponse("EMERGENCY",
                "Your description includes one or more emergency warning signs. Get immediate professional help.",
                guidance, nextSteps, followUp);
    }

    private String describeSymptoms(boolean fracture, boolean injury, boolean fever, boolean vomiting,
            boolean dizziness, boolean pain, boolean headache, boolean nausea) {
        List<String> labels = new ArrayList<>();
        if (fracture) labels.add("a possible fracture");
        if (injury) labels.add("an injury");
        if (fever) labels.add("a fever");
        if (vomiting) labels.add("vomiting");
        if (dizziness) labels.add("dizziness");
        if (headache) labels.add("a headache");
        else if (pain) labels.add("pain");
        if (nausea && !vomiting) labels.add("nausea");
        return String.join(", ", labels);
    }

    private SymptomTriageResponse buildResponse(String level, String reasoning, List<String> guidance,
            List<String> nextSteps, List<String> followUp) {
        return new SymptomTriageResponse(level, reasoning, guidance, nextSteps, followUp);
    }

    private String normalize(String text) {
        if (text == null) return "";
        return text.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s']", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private boolean hasAny(String text, String... options) {
        String paddedText = " " + text + " ";
        for (String option : options) {
            String paddedOption = " " + normalize(option) + " ";
            int index = paddedText.indexOf(paddedOption);
            while (index >= 0) {
                if (!isNegated(paddedText.substring(0, index))) return true;
                index = paddedText.indexOf(paddedOption, index + 1);
            }
        }
        return false;
    }

    private boolean isNegated(String textBeforeSymptom) {
        return textBeforeSymptom.matches("(?s).*\\b(?:no|not|without|denies|denied|negative for|does not|doesn t|never had)(?:\\s+[a-z]+){0,4}\\s*$");
    }
}
