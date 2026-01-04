import { Navigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

function PermissionRoute({ canAccess, children }) {
  const { user, loading } = useAuth();

  if (loading) return <div>Carregando...</div>;

  if (!user || !canAccess(user)) {
    return <Navigate to="/home" replace />;
  }

  return children;
}

export default PermissionRoute;
