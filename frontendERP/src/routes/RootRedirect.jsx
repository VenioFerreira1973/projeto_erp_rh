import { Navigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function RootRedirect() {
  const { user, primeiroAcesso } = useAuth();

  if (!user) {
    return <Navigate to="/login" replace />;
  }

  if (primeiroAcesso) {
    return <Navigate to="/onboarding" replace />; 
  }

  return <Navigate to="/home" replace />; 
}
