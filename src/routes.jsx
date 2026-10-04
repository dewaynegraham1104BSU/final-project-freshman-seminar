import { createBrowserRouter } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import SymptomCheckerPage from "./pages/SymptomCheckerPage";
import SchedulingPage from "./pages/SchedulingPage";
import MentalHealthResourcesPage from "./pages/MentalHealthResourcesPage";
import ProviderDashboardPage from "./pages/ProviderDashboardPage";
import EmergencyGuidancePage from "./pages/EmergencyGuidancePage";
import CareCenterPage from "./pages/CareCenterPage";

export const router = createBrowserRouter([
  { path: "/", element: <LoginPage /> },
  { path: "/care-center", element: <CareCenterPage /> },
  { path: "/symptom-checker", element: <SymptomCheckerPage /> },
  { path: "/scheduling", element: <SchedulingPage /> },
  { path: "/resources", element: <MentalHealthResourcesPage /> },
  { path: "/provider-dashboard", element: <ProviderDashboardPage /> },
  { path: "/emergency", element: <EmergencyGuidancePage /> }
]);
