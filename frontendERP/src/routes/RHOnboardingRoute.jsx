import { Navigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import { permissions } from "../auth/permissions";

export default function RHOnboardingRoute({ children }) {
  const { user, loading } = useAuth();

  if (loading) return null;

  if (!user) {
    return <Navigate to="/" replace />;
  }

  if (!permissions.onboarding.write(user)) {
    return <Navigate to="/home" replace />;
  }

  return children;
}
