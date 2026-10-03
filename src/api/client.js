import axios from "axios";

const BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

/**
 * We try backend first. If it fails, API functions will fallback to mock.
 */
export const apiClient = axios.create({
  baseURL: BASE_URL,
  timeout: 8000
});
