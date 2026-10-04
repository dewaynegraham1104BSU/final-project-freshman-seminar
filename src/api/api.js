import { apiClient } from "./client";
import * as mock from "./mockApi";

export async function loginApi(form) {
  // If I don’t have auth endpoints yet, just mock
  return mock.mockLogin(form);
}

export async function triageApi(payload) {
  // Most likely route from your annotations:
  // POST /api/triage
  try {
    const res = await apiClient.post("/api/triage", payload);
    return res.data;
  } catch (err) {
    return mock.mockTriage(payload);
  }
}

export async function getProviderAvailabilityApi() {
  // GET /api/providers/availability
  try {
    const res = await apiClient.get("/api/providers/availability");
    return res.data;
  } catch {
    return mock.mockProviderAvailability();
  }
}

export async function createAppointmentApi(input) {
  try {
    const res = await apiClient.post("/api/appointments", input);
    return res.data;
  } catch {
    return mock.mockCreateAppointment(input);
  }
}

export async function getMentalResourcesApi(query) {
  // GET /api/resources
  // backend expects a query param, adjust here.
  try {
    const res = await apiClient.get("/api/resources", {
      params: query ? { q: query } : {}
    });
    return res.data;
  } catch {
    return mock.mockMentalResources(query);
  }
}

export async function getProviderDashboardApi() {
  // needed to paste StaffStudent/Provider dashboard endpoints; keep mock for demo.
  return mock.mockProviderDashboard();
}

export async function getEmergencyGuidanceApi(triageLevel) {
  // @RequestMapping("/api/emergency")
  // needed for proper mapping
  // We'll try POST first with a body, then fallback to GET with query.
  try {
    const res = await apiClient.post("/api/emergency", { triageLevel });
    return res.data;
  } catch {
    try {
      const res = await apiClient.get("/api/emergency", { params: { triageLevel } });
      return res.data;
    } catch {
      return mock.mockEmergencyGuidance(triageLevel);
    }
  }
}
