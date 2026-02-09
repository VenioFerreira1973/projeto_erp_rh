import React from "react";
import { Navigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function PermissionRoute({ canAccess, children }: any): JSX.Element | null {
  const { user, loading } = useAuth() as any;

  if (loading) return null;

  if (!user || !canAccess(user)) {
    return <Navigate to="/home" replace />;
  }

  return children;
}
