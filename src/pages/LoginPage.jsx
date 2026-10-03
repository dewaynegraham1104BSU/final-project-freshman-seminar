import React from "react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginApi } from "../api/api";

export default function LoginPage() {
  const [username, setUsername] = useState("");
  const [message, setMessage] = useState("");
  const navigate = useNavigate();

  async function onSubmit(e) {
    e.preventDefault();
    setMessage("Logging in...");

    const res = await loginApi({ username });

    if (res?.token) {
      localStorage.setItem("token", res.token);
      navigate("/symptom-checker");
      return;
    }

    setMessage("Login failed.");
  }

  return (
    <div style={{ padding: 24, maxWidth: 520, margin: "0 auto" }}>
      <h1>Welcome to Bowie State Wellness</h1>

      <form onSubmit={onSubmit} style={{ display: "grid", gap: 12, marginTop: 18 }}>
        <label>
          Username
          <input
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </label>
        <button type="submit">Login</button>
      </form>

      {message && <p style={{ marginTop: 16 }}>{message}</p>}
    </div>
  );
}
