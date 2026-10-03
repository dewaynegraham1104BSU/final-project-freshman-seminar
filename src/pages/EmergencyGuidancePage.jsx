import React from "react";
import { useState } from "react";
import { getEmergencyGuidanceApi } from "../api/api";

export default function EmergencyGuidancePage() {
  const [level, setLevel] = useState("");
  const [data, setData] = useState(null);

  async function load() {
    const res = await getEmergencyGuidanceApi(level);
    setData(res);
  }

  return (
    <div style={{ padding: 24, maxWidth: 850, margin: "0 auto" }}>
      <h1>Emergency Guidance</h1>

      <div style={{ marginTop: 16, display: "grid", gap: 12 }}>
        <label>
          Triage Level (choose)
          <select value={level} onChange={(e) => setLevel(e.target.value)}>
            <option value="">Select</option>
            <option value="EMERGENCY">EMERGENCY</option>
            <option value="URGENT">URGENT</option>
            <option value="SELF_CARE">SELF_CARE</option>
          </select>
        </label>

        <button disabled={!level} onClick={load}>Show Guidance</button>

        {data && (
          <div style={{ border: "1px solid #333", borderRadius: 8, padding: 16 }}>
            <h2>{data.headline}</h2>
            <p>{data.message}</p>
            <ul>
              {(data.actions || []).map((a, i) => <li key={i}>{a}</li>)}
            </ul>
          </div>
        )}
      </div>
    </div>
  );
}
