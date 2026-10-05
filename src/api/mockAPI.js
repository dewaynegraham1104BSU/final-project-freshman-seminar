export function mockLogin(form) {
  // demo only
  return {
    ok: true,
    user: { id: "1", name: form.username || "Demo User", role: "student" },
    token: "mock-jwt-token"
  };
}

export async function mockTriage(payload) {
  const text = (payload.symptom || payload.symptomsText || payload.symptoms || "")
    .toLowerCase()
    .replace(/[^a-z0-9\s']/g, " ")
    .replace(/\s+/g, " ")
    .trim();

  function hasAny(phrases) {
    return phrases.some((phrase) => {
      let index = text.indexOf(phrase);
      while (index !== -1) {
        const before = text.slice(0, index);
        if (!/(?:^|\s)(?:no|not|without|denies|denied|negative for|does not|doesn t|never had)(?:\s+[a-z]+){0,4}\s*$/.test(before)) {
          return true;
        }
        index = text.indexOf(phrase, index + 1);
      }
      return false;
    });
  }

  const symptoms = {
    breathing: hasAny(["trouble breathing", "breathing trouble", "difficulty breathing", "shortness of breath", "short of breath", "cannot breathe", "can't breathe", "can't catch my breath", "gasping"]),
    chest: hasAny(["chest pain", "chest pressure", "pressure in my chest", "chest hurts"]),
    stroke: hasAny(["stroke", "face drooping", "slurred speech", "sudden weakness", "sudden numbness", "one sided weakness", "one sided numbness"]),
    unconscious: hasAny(["unconscious", "passed out", "fainted", "fainting", "not waking up"]),
    bleeding: hasAny(["severe bleeding", "bleeding heavily", "wont stop bleeding", "won't stop bleeding", "spurting blood"]),
    seizure: hasAny(["seizure", "convulsion"]),
    overdose: hasAny(["overdose", "poisoning", "poisoned"]),
    suicidal: hasAny(["suicidal", "suicide", "kill myself", "hurt myself", "self harm", "self-harm"]),
    allergic: hasAny(["throat swelling", "swollen throat", "tongue swelling", "swollen tongue", "anaphylaxis"]),
    severeHeadache: hasAny(["worst headache", "sudden severe headache"]),
    seriousInjury: hasAny(["bone through skin", "bone sticking out", "open fracture", "severe head injury", "head injury with confusion"]),
    fracture: hasAny(["broken bone", "fracture", "suspected fracture"]),
    injury: hasAny(["sprain", "injury", "injured", "burn", "deep cut", "cut"]),
    fever: hasAny(["fever", "high temperature"]),
    vomiting: hasAny(["vomiting", "throwing up", "cannot keep fluids down"]),
    dizziness: hasAny(["dizziness", "dizzy", "vertigo"]),
    pain: hasAny(["pain", "ache", "aching", "severe pain", "worsening pain", "sharp pain", "painful swelling", "severe stomach pain"]),
    headache: hasAny(["headache", "migraine"]),
    nausea: hasAny(["nausea", "nauseous"])
  };

  const emergency = symptoms.breathing || symptoms.chest || symptoms.stroke ||
    symptoms.unconscious || symptoms.bleeding || symptoms.seizure || symptoms.overdose ||
    symptoms.suicidal || symptoms.allergic || symptoms.severeHeadache || symptoms.seriousInjury;

  if (emergency) {
    const guidance = ["The symptoms described may indicate a life-threatening emergency. A text checker cannot assess you safely."];
    if (symptoms.breathing) guidance.push("Breathing difficulty needs immediate emergency assessment.");
    if (symptoms.chest) guidance.push("New or severe chest pain or pressure can be an emergency.");
    if (symptoms.stroke) guidance.push("Sudden face, speech, or one-sided strength changes can be signs of a stroke.");
    if (symptoms.unconscious) guidance.push("Loss of consciousness or not waking up needs immediate emergency help.");
    if (symptoms.bleeding) guidance.push("Severe or uncontrolled bleeding needs immediate emergency treatment.");
    if (symptoms.seizure) guidance.push("A seizure may need emergency care, especially if it is ongoing, repeated, or the person is injured.");
    if (symptoms.overdose) guidance.push("A possible overdose or poisoning needs immediate expert help, even if the person feels okay.");
    if (symptoms.suicidal) guidance.push("If you may harm yourself or cannot stay safe, get immediate crisis support and do not stay alone.");
    if (symptoms.allergic) guidance.push("Swelling of the throat or tongue may block breathing and needs emergency care.");
    if (symptoms.severeHeadache) guidance.push("A sudden, unusually severe headache can be an emergency.");
    if (symptoms.seriousInjury) guidance.push("The described injury needs immediate in-person emergency assessment.");

    const nextSteps = [
      "Call your local emergency number now or have someone nearby call. Do not drive yourself.",
      "Stay with another person and follow the emergency dispatcher's instructions."
    ];
    if (symptoms.overdose) nextSteps.push("Contact your local poison control center as well; do not induce vomiting unless a professional tells you to.");
    if (symptoms.suicidal) nextSteps.push("In the U.S. or Canada, call or text 988 for crisis support; call emergency services if danger is immediate.");
    return {
      triageLevel: "EMERGENCY",
      level: "EMERGENCY",
      reasoningSummary: "Your description includes one or more emergency warning signs. Get immediate professional help.",
      guidance,
      nextSteps,
      followUpQuestions: ["Tell emergency responders when the symptoms began and any relevant medications, substances, or injuries."]
    };
  }

  const guidance = [];
  const nextSteps = [];
  const followUpQuestions = [];
  const labels = [];
  if (symptoms.fracture) {
    labels.push("a possible fracture");
    guidance.push("A suspected broken bone needs an in-person medical assessment; this description cannot confirm a fracture.");
    nextSteps.push("Keep the injured area still in the position you found it. Do not try to straighten it or push a bone back into place.");
    nextSteps.push("Get urgent medical care today. Call emergency services if the bone is exposed, bleeding is severe, or the limb becomes numb, pale, or cold.");
    followUpQuestions.push("When did the injury happen, and where is the suspected fracture?");
  }
  if (symptoms.injury && !symptoms.fracture) {
    labels.push("an injury");
    guidance.push("The injury you described may need examination, especially if the wound is deep, the burn is large, or movement is difficult.");
    nextSteps.push("Protect the injured area and arrange prompt medical evaluation if it is deep, worsening, or limiting movement.");
    followUpQuestions.push("How did the injury happen, and is the affected area getting worse?");
  }
  if (symptoms.fever) {
    labels.push("a fever");
    guidance.push("A fever can have many causes; its severity, duration, and other symptoms affect what care is appropriate.");
    nextSteps.push("Rest, drink fluids if you can, and contact a clinician promptly if the fever is high, persists, or you feel significantly worse.");
    followUpQuestions.push("How long have you had the fever, and have you measured your temperature?");
  }
  if (symptoms.vomiting) {
    labels.push("vomiting");
    guidance.push("Vomiting can lead to dehydration, particularly if you cannot keep liquids down.");
    nextSteps.push("Take small, frequent sips of fluid if you can. Seek prompt medical care if vomiting continues, you cannot keep fluids down, or you notice signs of dehydration or blood.");
    followUpQuestions.push("How long have you been vomiting, and can you keep fluids down?");
  }
  if (symptoms.dizziness) {
    labels.push("dizziness");
    guidance.push("Dizziness can have different causes; sudden or worsening symptoms should be assessed by a clinician.");
    nextSteps.push("Sit or lie down to reduce the risk of falling, and arrange prompt medical care if the dizziness persists or worsens.");
    followUpQuestions.push("Did the dizziness start suddenly, and are you having trouble walking or staying alert?");
  }
  if (symptoms.pain || symptoms.headache) {
    labels.push(symptoms.headache ? "a headache" : "pain");
    guidance.push("The pain you described cannot be diagnosed from text. Severe, worsening, unusual, or persistent pain should be assessed promptly.");
    nextSteps.push("Avoid activities that worsen the pain and arrange medical evaluation, especially if it is severe, worsening, or persistent.");
    followUpQuestions.push("Where is the pain, when did it begin, and is it getting worse?");
  }
  if (symptoms.nausea && !symptoms.vomiting) {
    labels.push("nausea");
    guidance.push("Nausea has many possible causes; monitor for vomiting, dehydration, or worsening symptoms.");
    nextSteps.push("Try small sips of fluid if tolerated and contact a clinician if nausea persists or worsens.");
    followUpQuestions.push("How long have you felt nauseated, and can you keep fluids down?");
  }

  const urgent = symptoms.fracture || symptoms.injury || symptoms.fever || symptoms.vomiting ||
    symptoms.dizziness || symptoms.pain || symptoms.headache || symptoms.nausea;
  if (!urgent) {
    guidance.push("The description does not identify an emergency warning sign, but a text checker cannot determine the cause or rule out a serious condition.");
    nextSteps.push("Monitor your symptoms and arrange a routine appointment if they persist, return, or interfere with daily activities.");
    nextSteps.push("Seek urgent care if symptoms become severe or worsen; call emergency services for trouble breathing, severe chest pain, fainting, or other life-threatening symptoms.");
    followUpQuestions.push("When did the symptoms start, and are they improving or getting worse?");
  }

  return {
    triageLevel: urgent ? "URGENT" : "SELF_CARE",
    level: urgent ? "URGENT" : "SELF_CARE",
    reasoningSummary: urgent
      ? `Your description mentions ${labels.join(", ")}. Prompt medical evaluation is recommended; seek emergency care for severe or rapidly worsening symptoms.`
      : "No specific emergency warning sign was recognized in your description. This does not rule out a medical problem.",
    guidance,
    nextSteps,
    followUpQuestions
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
