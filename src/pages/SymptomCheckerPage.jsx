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

  const renderGuidance = () => {
    if (!result) return null;

    const guidance = Array.isArray(result.guidance) ? result.guidance : [];

    if (guidance.length > 0) {
      return (
        <ul>
          {guidance.map((s, i) => (
            <li key={i}>{s}</li>
          ))}
        </ul>
      );
    }

    return <p>—</p>;
  };

  const renderNextSteps = () => {
    if (!result) return null;

    const nextSteps = Array.isArray(result.nextSteps)
      ? result.nextSteps
      : Array.isArray(result.followUpQuestions)
        ? result.followUpQuestions
        : [];

    if (nextSteps.length > 0) {
      return (
        <ul>
          {nextSteps.map((s, i) => (
            <li key={i}>{s}</li>
          ))}
        </ul>
      );
    }

    return <p>—</p>;
  };

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
          placeholder="Example: I have a fever and pain..."
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

          <h3>Guidance</h3>
          {renderGuidance()}

          <h3>Next Steps</h3>
          {renderNextSteps()}
        </div>
      )}
    </div>
  );
}
