// frontend/src/api/api.js

import axios from "axios";

export const api = axios.create({
  baseURL: "http://localhost:8080",
});

export async function getAppointments() {
  return api.get("/YOUR_PATH_HERE");
}

export async function triageSymptom({ symptom, durationHours }) {
  return api.post("/api/triage", {
    symptom,
    durationHours,
  });
}
