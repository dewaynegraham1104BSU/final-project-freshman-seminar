import React from "react";
import { Outlet, Link } from "react-router-dom";

export default function App() {
  return (
    <div>
      <header style={{ padding: 14, borderBottom: "1px solid #333" }}>
        <nav style={{ display: "flex", gap: 14, flexWrap: "wrap" }}>
          <Link to="/">Login</Link>
          <Link to="/symptom-checker">Symptom Checker</Link>
          <Link to="/scheduling">Scheduling</Link>
          <Link to="/resources">Resources</Link>
          <Link to="/provider-dashboard">Provider Dashboard</Link>
          <Link to="/emergency">Emergency</Link>
        </nav>
      </header>
      <Outlet />
    </div>
  );
}
