import React, { useState } from "react";
import SymptomCheckerPage from "./SymptomCheckerPage";
import SchedulingPage from "./SchedulingPage";
import MentalHealthResourcesPage from "./MentalHealthResourcesPage";

const mentalHealthKeywords = {
  emergency: ["suicidal", "kill myself", "self-harm", "hurt myself", "can't go on", "hopeless"],
  urgent: ["panic", "anxious", "very stressed", "crying a lot", "overwhelmed", "depressed", "sad all the time"],
  low: ["tired", "stressed", "busy", "sleepy", "overloaded"]
};

export default function CareCenterPage() {
  const [mainTab, setMainTab] = useState("medical");
  const [medicalTab, setMedicalTab] = useState("symptoms");
  const [mentalInput, setMentalInput] = useState("");
  const [mentalResult, setMentalResult] = useState(null);

  function assessMentalHealth() {
    const text = mentalInput.toLowerCase();
    const isEmergency = mentalHealthKeywords.emergency.some((k) => text.includes(k));
    const isUrgent = mentalHealthKeywords.urgent.some((k) => text.includes(k));

    const level = isEmergency ? "EMERGENCY" : isUrgent ? "URGENT" : "SELF_CARE";

    const resultMap = {
      EMERGENCY: {
        title: "Immediate Support Needed",
        guidance: [
          "If you feel in immediate danger or may hurt yourself, call emergency services now.",
          "Go to the nearest ER or contact a crisis line right away.",
          "If possible, tell a trusted person and stay with someone safe."
        ],
        nextSteps: [
          "Call 988 or local crisis support immediately.",
          "Use the emergency guidance page or call campus emergency services.",
          "Do not stay alone if you feel unsafe."
        ]
      },
      URGENT: {
        title: "Needs Prompt Support",
        guidance: [
          "Your symptoms suggest the need for mental health support soon.",
          "A counselor or clinician can help you make a plan.",
          "Try grounding techniques while you reach out for help."
        ],
        nextSteps: [
          "Schedule a counseling or campus support appointment.",
          "Talk to a trusted friend, family member, or advisor.",
          "If your feelings intensify, seek urgent care or crisis help."
        ]
      },
      SELF_CARE: {
        title: "Supportive Self-Care",
        guidance: [
          "You may benefit from rest, calming routines, and time to reset.",
          "Try grounding and simple daily coping strategies.",
          "Monitor your mood and stress level for the next day or two."
        ],
        nextSteps: [
          "Try breathing exercises, sleep support, and regular meals.",
          "Use the mental health resources page for coping tools.",
          "Reach out for support if symptoms get worse or last longer than expected."
        ]
      }
    };

    setMentalResult(resultMap[level]);
  }

  const renderMedicalContent = () => {
    if (medicalTab === "symptoms") return <SymptomCheckerPage />;
    if (medicalTab === "scheduling") return <SchedulingPage />;
    return <MentalHealthResourcesPage />;
  };

  const renderMentalContent = () => (
    <div style={{ padding: 20, border: "1px solid #ccc", borderRadius: 10, maxWidth: 720, margin: "24px auto" }}>
      <h2>Mental Health Support</h2>
      <label style={{ display: "block", marginBottom: 8, fontWeight: 600 }}>
        How are you feeling today?
      </label>
      <textarea
        value={mentalInput}
        onChange={(e) => setMentalInput(e.target.value)}
        rows={6}
        style={{ width: "100%", padding: 10, borderRadius: 8, resize: "vertical" }}
        placeholder="Example: I feel overwhelmed and have panic attacks..."
      />
      <button onClick={assessMentalHealth} style={{ marginTop: 12, padding: "10px 16px", borderRadius: 8, cursor: "pointer" }}>
        Check My Mental Health Support Needs
      </button>

      {mentalResult && (
        <div style={{ marginTop: 20, padding: 16, border: "1px solid #d0d0d0", borderRadius: 8, background: "#f9f9f9" }}>
          <h3>{mentalResult.title}</h3>
          <p><strong>Suggested level:</strong> {mentalResult.title === "Immediate Support Needed" ? "EMERGENCY" : mentalResult.title === "Needs Prompt Support" ? "URGENT" : "SELF_CARE"}</p>
          <h4>Guidance</h4>
          <ul>
            {mentalResult.guidance.map((g, i) => <li key={i}>{g}</li>)}
          </ul>
          <h4>Next Steps</h4>
          <ul>
            {mentalResult.nextSteps.map((n, i) => <li key={i}>{n}</li>)}
          </ul>
        </div>
      )}
    </div>
  );

  return (
    <div style={{ maxWidth: 1200, margin: "0 auto", padding: 24 }}>
      <h1>Care Center</h1>

      <div style={{ display: "flex", gap: 10, flexWrap: "wrap", marginBottom: 18 }}>
        <button onClick={() => setMainTab("medical")} style={{ padding: "10px 18px", borderRadius: 8, background: mainTab === "medical" ? "#2d6cdf" : "#e5e7eb", color: mainTab === "medical" ? "white" : "#111" }}>
          Medical
        </button>
        <button onClick={() => setMainTab("mental")} style={{ padding: "10px 18px", borderRadius: 8, background: mainTab === "mental" ? "#7c3aed" : "#e5e7eb", color: mainTab === "mental" ? "white" : "#111" }}>
          Mental Health
        </button>
      </div>

      {mainTab === "medical" && (
        <div>
          <div style={{ display: "flex", gap: 10, flexWrap: "wrap", marginBottom: 18 }}>
            {[
              { key: "symptoms", label: "Symptoms" },
              { key: "scheduling", label: "Scheduling" },
              { key: "resources", label: "Resources" }
            ].map((tab) => (
              <button
                key={tab.key}
                onClick={() => setMedicalTab(tab.key)}
                style={{ padding: "8px 14px", borderRadius: 8, background: medicalTab === tab.key ? "#111827" : "#f3f4f6", color: medicalTab === tab.key ? "white" : "#111827" }}
              >
                {tab.label}
              </button>
            ))}
          </div>
          {renderMedicalContent()}
        </div>
      )}

      {mainTab === "mental" && renderMentalContent()}
    </div>
  );
}
