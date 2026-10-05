import React from "react";
import { useState, useEffect } from "react";
import { getMentalResourcesApi } from "../api/api";

const externalResources = {
  "Mental Health": [
    {
      title: "Find Mental Health Support",
      organization: "National Institute of Mental Health",
      description: "Learn how to find help and connect with mental health services.",
      url: "https://www.nimh.nih.gov/health/find-help",
      keywords: ["mental", "counseling", "counsellor", "therapy", "therapist", "support", "help", "general"],
      isDefault: true
    },
    {
      title: "Anxiety Disorders",
      organization: "National Institute of Mental Health",
      description: "Information about anxiety disorders, symptoms, and treatment.",
      url: "https://www.nimh.nih.gov/health/topics/anxiety-disorders",
      keywords: ["anxiety", "anxious", "panic", "worry", "stress"]
    },
    {
      title: "Depression",
      organization: "National Institute of Mental Health",
      description: "Learn about depression symptoms and treatment options.",
      url: "https://www.nimh.nih.gov/health/topics/depression",
      keywords: ["depression", "depressed", "sad", "mood", "hopeless"]
    },
    {
      title: "988 Suicide & Crisis Lifeline",
      organization: "988 Lifeline",
      description: "Call or text 988 for free, confidential crisis support in the United States.",
      url: "https://988lifeline.org/",
      keywords: ["crisis", "suicide", "suicidal", "self-harm", "self harm", "unsafe", "immediate"]
    },
    {
      title: "Sleep and Mental Health",
      organization: "National Institute of Mental Health",
      description: "Explore information about sleep and mental health.",
      url: "https://www.nimh.nih.gov/health/topics",
      keywords: ["sleep", "insomnia", "tired", "nightmares"]
    }
  ],
  Medical: [
    {
      title: "Health Topics",
      organization: "MedlinePlus",
      description: "Reliable information about conditions, symptoms, tests, and treatments.",
      url: "https://medlineplus.gov/healthtopics.html",
      keywords: ["medical", "health", "symptom", "symptoms", "pain", "doctor", "condition", "general"],
      isDefault: true
    },
    {
      title: "Find a Health Center",
      organization: "Health Resources & Services Administration",
      description: "Find a health center offering primary care services near you.",
      url: "https://findahealthcenter.hrsa.gov/",
      keywords: ["doctor", "clinic", "appointment", "provider", "care", "health center", "medical"]
    },
    {
      title: "Common Health Topics",
      organization: "MedlinePlus",
      description: "Browse trusted guides for common illnesses and health concerns.",
      url: "https://medlineplus.gov/",
      keywords: ["fever", "cold", "flu", "headache", "stomach", "nausea", "cough", "allergy", "injury", "infection"]
    },
    {
      title: "Centers for Disease Control and Prevention",
      organization: "CDC",
      description: "Find public health information and guidance on illnesses and prevention.",
      url: "https://www.cdc.gov/",
      keywords: ["virus", "covid", "prevention", "vaccine", "illness", "disease"]
    }
  ]
};

function getRelevantResources(resources, query) {
  const normalizedQuery = query.trim().toLowerCase();
  if (!normalizedQuery) return resources;

  const matches = resources.filter((resource) =>
    resource.keywords.some((keyword) =>
      normalizedQuery.includes(keyword) || keyword.includes(normalizedQuery)
    )
  );

  return matches.length ? matches : resources.filter((resource) => resource.isDefault);
}

export default function MentalHealthResourcesPage() {
  const [q, setQ] = useState("");
  const [items, setItems] = useState([]);

  async function search() {
    const res = await getMentalResourcesApi(q);
    setItems(Array.isArray(res) ? res : res?.items || []);
  }

  useEffect(() => { search(); }, []);

  return (
    <div style={{ padding: 24, maxWidth: 900, margin: "0 auto" }}>
      <h1>Mental Health Resources</h1>

      <div style={{ display: "flex", gap: 10, marginTop: 16 }}>
        <input
          value={q}
          onChange={(e) => setQ(e.target.value)}
          onKeyDown={(e) => { if (e.key === "Enter") search(); }}
          placeholder="Search resources..."
          style={{ flex: 1 }}
        />
        <button type="button" onClick={search}>Search</button>
      </div>

      <section aria-labelledby="helpful-links-heading" style={{ marginTop: 24 }}>
        <h2 id="helpful-links-heading">Helpful Links</h2>
        <p>Links are suggested based on your search. For urgent or emergency symptoms, contact emergency services.</p>
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(260px, 1fr))", gap: 16 }}>
          {Object.entries(externalResources).map(([category, resources]) => (
            <section key={category} aria-label={`${category} resources`} style={{ border: "1px solid #ccc", borderRadius: 8, padding: 16 }}>
              <h3>{category}</h3>
              <div style={{ display: "grid", gap: 12 }}>
                {getRelevantResources(resources, q).map((resource) => (
                  <article key={resource.url}>
                    <a href={resource.url} target="_blank" rel="noopener noreferrer">
                      {resource.title}
                    </a>
                    <p style={{ margin: "4px 0" }}><strong>{resource.organization}</strong></p>
                    <p style={{ margin: 0 }}>{resource.description}</p>
                  </article>
                ))}
              </div>
            </section>
          ))}
        </div>
      </section>

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
