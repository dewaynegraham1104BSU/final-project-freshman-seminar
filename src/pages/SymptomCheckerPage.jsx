import React, { useState } from "react";
import { triageApi } from "../api/api";

export default function SymptomCheckerPage() {
  const [symptomsText, setSymptomsText] = useState("");
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);

  async function onCheck(e) {
    e.preventDefault();

    const cleaned = symptomsText.trim(); // allow any text, just prevent empty submissions
    if (!cleaned) {
      alert("Please describe your symptoms.");
      return;
    }

    setLoading(true);
    setResult(null);

    try {
      const data = await triageApi({
        symptom: cleaned,
        durationHours: 1,
      });

      setResult(data);
    } catch (err) {
      console.error("Triage failed:", err);
      alert(`Triage failed: ${err.message}`);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ padding: 24, maxWidth: 780, margin: "0 auto" }}>
      <h1>AI Symptom Checker</h1>

      <form onSubmit={onCheck} style={{ marginTop: 16, display: "grid", gap: 12 }}>
        <label htmlFor="symptomsTextArea">Describe your symptoms</label>

        <textarea
          id="symptomsTextArea"
          name="symptomsText"
          value={symptomsText}
          onChange={(e) => setSymptomsText(e.target.value)}
          rows={6}
          placeholder="Describe each symptom, when it started, and whether it is getting worse..."
          required
        />

        <button disabled={loading} type="submit">
          {loading ? "Triaging..." : "Check Symptoms"}
        </button>
      </form>

      {result && (
        <div
          style={{
            marginTop: 18,
            padding: 16,
            border: "1px solid #333",
            borderRadius: 8,
          }}
        >
          <h2>Result</h2>
          <p>
            <b>Triage Level:</b> {result.level || result.triageLevel || "—"}
          </p>
          {result.reasoningSummary && (
            <p><b>Based on your description:</b> {result.reasoningSummary}</p>
          )}

          <h3>Guidance</h3>
          {Array.isArray(result.guidance) && result.guidance.length > 0 ? (
            <ul>
              {result.guidance.map((item, i) => <li key={i}>{item}</li>)}
            </ul>
          ) : (
            <p>Contact a healthcare professional for guidance specific to your symptoms.</p>
          )}

          <h3>Next Steps</h3>
          {Array.isArray(result.nextSteps) && result.nextSteps.length > 0 ? (
            <ul>
              {result.nextSteps.map((item, i) => <li key={i}>{item}</li>)}
            </ul>
          ) : (
            <p>Arrange a medical assessment to discuss your symptoms and appropriate next steps.</p>
          )}

          <p><small>This symptom checker provides general information, not a diagnosis. If symptoms are severe or rapidly worsening, seek emergency care.</small></p>
        </div>
      )}
    </div>
  );
}
