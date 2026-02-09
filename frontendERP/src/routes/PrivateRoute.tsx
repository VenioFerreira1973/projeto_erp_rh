import { Navigate, useLocation } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function PrivateRoute({ children }: { children: any }) {
  const { user, primeiroAcesso, loading } = useAuth();
  const location = useLocation();

  if (loading) return null;

  if (!user) { return <Navigate to="/login" replace />; }

  const isNaPaginaDeSenha = location.pathname === "/primeiro-acesso";
  const isNoAdmissao = location.pathname.startsWith("/admissao");

  if (primeiroAcesso) {
    if (isNaPaginaDeSenha || isNoAdmissao) {
      return children;
    }
    return <Navigate to="/admissao" replace />;
  }

  return children;
}
