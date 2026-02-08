import { Navigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function PermissionRoute({ canAccess, children }) {
  const { user, loading } = useAuth();

  if (loading) return null; 

  if (!user || !canAccess(user)) {
    return <Navigate to="/home" replace />;
  }

  return children;
}
