import React from "react";
import { useState, useEffect } from "react";
import { getProviderDashboardApi } from "../api/api";

export default function ProviderDashboardPage() {
  const [data, setData] = useState(null);

  useEffect(() => {
    (async () => {
      const res = await getProviderDashboardApi();
      setData(res);
    })();
  }, []);

  return (
    <div style={{ padding: 24, maxWidth: 950, margin: "0 auto" }}>
      <h1>Provider Availability Dashboard</h1>

      {!data ? (
        <p>Loading...</p>
      ) : (
        <div style={{ display: "grid", gap: 18 }}>
          <section>
            <h2>Upcoming Appointments</h2>
            <ul>
              {(data.upcomingAppointments || []).map((a) => (
                <li key={a.id}>
                  {a.student} — {a.provider} — {a.date} {a.time}
                </li>
              ))}
            </ul>
          </section>

          <section>
            <h2>Availability</h2>
            <div style={{ display: "grid", gap: 12 }}>
              {(data.availability || []).map((p) => (
                <div key={p.id} style={{ border: "1px solid #333", borderRadius: 8, padding: 16 }}>
                  <p><b>{p.name}</b> ({p.type})</p>
                  <p>Slots: {(p.slots || []).join(", ")}</p>
                </div>
              ))}
            </div>
          </section>
        </div>
      )}
    </div>
  );
}
