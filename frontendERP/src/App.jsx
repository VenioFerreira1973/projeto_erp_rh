import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "./auth/AuthContext";
import PermissionRoute from "./routes/PermissionRoute";
import { permissions } from "./auth/permissions";

import Login from "./pages/Login";
import Home from "./pages/Home";
import Funcionarios from "./pages/Funcionarios";

function PrivateRoute({ children }) {
  const { user, loading } = useAuth();

  if (loading) return <div>Carregando...</div>;
  return user ? children : <Navigate to="/" />;
}

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />

        <Route
          path="/home"
          element={
            <PrivateRoute>
              <Home />
            </PrivateRoute>
          }
        />

        <Route
          path="/funcionarios"
          element={
            <PrivateRoute>
              <PermissionRoute
                canAccess={(user) => permissions.funcionario.admin(user)}
              >
                <Funcionarios />
              </PermissionRoute>
            </PrivateRoute>
          }
        />
      </Routes>
    </Router>
  );
}

export default App;
