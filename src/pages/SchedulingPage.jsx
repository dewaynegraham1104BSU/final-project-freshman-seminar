import React from "react";
import { useState, useEffect } from "react";
import { getProviderAvailabilityApi, createAppointmentApi } from "../api/api";

export default function SchedulingPage() {
  const [providers, setProviders] = useState([]);
  const [selectedProviderId, setSelectedProviderId] = useState("");
  const [selectedTime, setSelectedTime] = useState("");
  const [date, setDate] = useState(new Date().toISOString().slice(0, 10));
  const [notes, setNotes] = useState("");
  const [msg, setMsg] = useState("");

  useEffect(() => {
    (async () => {
      const res = await getProviderAvailabilityApi();
      setProviders(res || []);
    })();
  }, []);

  const selectedProvider = providers.find((p) => p.id === selectedProviderId);

  async function book() {
    setMsg("Booking...");
    const res = await createAppointmentApi({
      userId: "1",
      providerId: selectedProviderId,
      providerName: selectedProvider?.name,
      type: selectedProvider?.type,
      date,
      time: selectedTime,
      notes
    });
    setMsg(res?.ok ? "Appointment booked successfully!" : "Booking failed.");
  }

  return (
    <div style={{ padding: 24, maxWidth: 900, margin: "0 auto" }}>
      <h1>Appointment Scheduling</h1>

      <div style={{ display: "grid", gap: 14, marginTop: 16 }}>
        <label>
          Date
          <input type="date" value={date} onChange={(e) => setDate(e.target.value)} />
        </label>

        <label>
          Provider
          <select value={selectedProviderId} onChange={(e) => { setSelectedProviderId(e.target.value); setSelectedTime(""); }}>
            <option value="">Select a provider</option>
            {providers.map((p) => (
              <option key={p.id} value={p.id}>{p.name} ({p.type})</option>
            ))}
          </select>
        </label>

        <label>
          Time
          <select value={selectedTime} onChange={(e) => setSelectedTime(e.target.value)} disabled={!selectedProvider}>
            <option value="">Select a slot</option>
            {(selectedProvider?.slots || []).map((t) => (
              <option key={t} value={t}>{t}</option>
            ))}
          </select>
        </label>

        <label>
          Notes (optional)
          <input value={notes} onChange={(e) => setNotes(e.target.value)} placeholder="Anything you want providers to know" />
        </label>

        <button disabled={!selectedProviderId || !selectedTime} onClick={book}>
          Book Appointment
        </button>

        {msg && <p>{msg}</p>}
      </div>
    </div>
  );
}
