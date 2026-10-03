import React, { useState } from "react";

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
      const res = await fetch("http://localhost:8080/api/triage", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          symptom: cleaned,
          durationHours: 1,
        }),
      });

      if (!res.ok) {
        const errText = await res.text().catch(() => "");
        throw new Error(errText || `HTTP ${res.status}`);
      }

      const data = await res.json(); // assuming backend returns JSON
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

    const guidance = result.guidance;

    if (Array.isArray(guidance) && guidance.length > 0) {
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
            <b>Triage Level:</b> {result.level || "—"}
          </p>

          <h3>Guidance</h3>
          {renderGuidance()}

          <h3>Next Steps</h3>
          <p>{result.nextStep || "—"}</p>
        </div>
      )}
    </div>
  );
}
