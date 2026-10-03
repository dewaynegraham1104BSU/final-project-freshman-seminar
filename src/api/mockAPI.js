export function mockLogin(form) {
  // demo only
  return {
    ok: true,
    user: { id: "1", name: form.username || "Demo User", role: "student" },
    token: "mock-jwt-token"
  };
}

export async function mockTriage(payload) {
  // Very simple rule-based demo
  const text = (payload.symptomsText || "").toLowerCase();
  const urgency =
    text.includes("chest") || text.includes("shortness of breath") || text.includes("suicidal")
      ? "EMERGENCY"
      : text.includes("pain") || text.includes("fever")
        ? "URGENT"
        : "SELF_CARE";

  return {
    triageLevel: urgency,
    suggestedService:
      urgency === "EMERGENCY" ? "Emergency Care" :
      urgency === "URGENT" ? "Campus Medical / Urgent Care" :
      "Self Care / Wellness Resources",
    nextSteps:
      urgency === "EMERGENCY"
        ? ["Call emergency services (911).", "Seek immediate care.", "If possible, have someone stay with you."]
        : urgency === "URGENT"
          ? ["Schedule an appointment.", "If symptoms worsen, seek urgent care."]
          : ["Use self-help steps.", "Monitor symptoms.", "Consider non-emergency appointment if needed."]
  };
}

export async function mockProviderAvailability() {
  return [
    { id: "p1", name: "Dr. Smith (Medical)", type: "MEDICAL", slots: ["10:00", "11:00", "14:00"] },
    { id: "p2", name: "Counselor Lee (Mental Health)", type: "MENTAL", slots: ["09:00", "13:00", "15:00"] }
  ];
}

export async function mockCreateAppointment(input) {
  return {
    ok: true,
    appointment: {
      id: "a1",
      userId: input.userId || "1",
      providerId: input.providerId,
      providerName: input.providerName,
      type: input.type,
      date: input.date,
      time: input.time,
      notes: input.notes || ""
    }
  };
}

export async function mockMentalResources(query) {
  const items = [
    { id: "r1", title: "Stress Management", category: "Mental Health", description: "Breathing, journaling, routines." },
    { id: "r2", title: "Coping With Anxiety", category: "Anxiety", description: "Grounding techniques and support." },
    { id: "r3", title: "Sleep Tips", category: "Self Care", description: "Healthy sleep hygiene basics." }
  ];
  if (!query) return items;
  const q = query.toLowerCase();
  return items.filter(x => x.title.toLowerCase().includes(q) || x.description.toLowerCase().includes(q));
}

export async function mockEmergencyGuidance(level) {
  const L = level?.toUpperCase?.() || "SELF_CARE";
  if (L === "EMERGENCY") {
    return {
      headline: "Emergency Guidance",
      message: "If you are in immediate danger, call 911 or campus emergency services now.",
      actions: ["Call 911.", "Go to the nearest ER.", "Tell someone near you what’s happening."]
    };
  }
  if (L === "URGENT") {
    return {
      headline: "Urgent Guidance",
      message: "Your symptoms may require timely evaluation.",
      actions: ["Schedule an appointment.", "If symptoms worsen, seek urgent care."]
    };
  }
  return {
    headline: "Self-Care Guidance",
    message: "You can start with self-care steps while monitoring symptoms.",
    actions: ["Rest and hydrate.", "Use resources below.", "Seek care if you worsen."]
  };
}

export async function mockProviderDashboard() {
  return {
    upcomingAppointments: [
      { id: "x1", student: "Jordan", provider: "Dr. Smith", date: "2026-10-05", time: "11:00" }
    ],
    availability: await mockProviderAvailability()
  };
}
