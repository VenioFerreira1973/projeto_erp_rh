import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function PrivateRoute({ children }) {
  const { user, primeiroAcesso, loading } = useAuth();
  const location = useLocation();

  if (loading) return null;

  if (!user) { return <Navigate to="/login" replace />; }

  const isNaPaginaDeSenha = location.pathname === "/primeiro-acesso";
  const isNoOnboarding = location.pathname.startsWith("/onboarding");

  if (primeiroAcesso) {
    if (isNaPaginaDeSenha || isNoOnboarding) {
      return children;
    }
    return <Navigate to="/onboarding" replace />;
  }

  return children;
}
