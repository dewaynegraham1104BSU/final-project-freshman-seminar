import React from "react";
import ReactDOM from "react-dom/client";
import { RouterProvider, createBrowserRouter } from "react-router-dom";
import App from "./app";
import LoginPage from "./pages/LoginPage";
import SymptomCheckerPage from "./pages/SymptomCheckerPage";
import SchedulingPage from "./pages/SchedulingPage";
import MentalHealthResourcesPage from "./pages/MentalHealthResourcesPage";
import ProviderDashboardPage from "./pages/ProviderDashboardPage";
import EmergencyGuidancePage from "./pages/EmergencyGuidancePage";

const router = createBrowserRouter([
  {
    element: <App />,
    children: [
      { index: true, element: <LoginPage /> },
      { path: "symptom-checker", element: <SymptomCheckerPage /> },
      { path: "scheduling", element: <SchedulingPage /> },
      { path: "resources", element: <MentalHealthResourcesPage /> },
      { path: "provider-dashboard", element: <ProviderDashboardPage /> },
      { path: "emergency", element: <EmergencyGuidancePage /> }
    ]
  }
]);

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
)

