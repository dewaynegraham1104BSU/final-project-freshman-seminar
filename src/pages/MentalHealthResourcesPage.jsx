import React from "react";
import { useState, useEffect } from "react";
import { getMentalResourcesApi } from "../api/api";

export default function MentalHealthResourcesPage() {
  const [q, setQ] = useState("");
  const [items, setItems] = useState([]);

  async function search() {
    const res = await getMentalResourcesApi(q);
    setItems(res || []);
  }

  useEffect(() => { search(); }, []);

  return (
    <div style={{ padding: 24, maxWidth: 900, margin: "0 auto" }}>
      <h1>Mental Health Resources</h1>

      <div style={{ display: "flex", gap: 10, marginTop: 16 }}>
        <input value={q} onChange={(e) => setQ(e.target.value)} placeholder="Search resources..." style={{ flex: 1 }} />
        <button onClick={search}>Search</button>
      </div>

      <div style={{ marginTop: 18, display: "grid", gap: 12 }}>
        {items.map((r) => (
          <div key={r.id} style={{ border: "1px solid #333", borderRadius: 8, padding: 16 }}>
            <h3>{r.title}</h3>
            <p><b>Category:</b> {r.category}</p>
            <p>{r.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
