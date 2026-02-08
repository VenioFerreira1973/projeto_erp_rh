import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import { AuthProvider } from "./auth/AuthContext";
import PrivateRoute from "./routes/PrivateRoute";
import PermissionRoute from "./routes/PermissionRoute";
import { permissions } from "./auth/permissions";
import { OnboardingProvider } from "./context/OnboardingContext";
import RootRedirect from "./routes/RootRedirect";
import AppLayout from "./components/AppLayout";

import Login from "./pages/Login";
import Home from "./pages/Home";
import Colaboradores from "./pages/Colaboradores";
import PrimeiroAcesso from "./pages/PrimeiroAcesso";
import EsqueciSenha from "./pages/EsqueciSenha";
import ResetarSenha from "./pages/ResetarSenha";
import OnboardingRouter from "./pages/onboarding/OnboardingRouter";
import RHOnboardingRoute from "./routes/RHOnboardingRoute";
import RHOnboardingPage from "./pages/RHOnboardingPage";
import RHOnboardingDashboard from "./pages/RHOnboardingDashboard";

import './App.css';

function App() {
  return (
    <AuthProvider>
      <OnboardingProvider>
        <Router>
          <Routes>
            {/* 🟢 Rotas PÚBLICAS (Sem Sidebar) */}
            <Route path="/login" element={<Login />} />
            <Route path="/" element={<RootRedirect />} />
            <Route path="/esqueci-senha" element={<EsqueciSenha />} />
            <Route path="/resetar-senha" element={<ResetarSenha />} />
            <Route path="/primeiro-acesso" element={<PrimeiroAcesso />} />

            {/* 🔐 Rotas PRIVADAS (Todas com Sidebar automática) */}
            <Route element={<PrivateRoute><AppLayout /></PrivateRoute>}>

              <Route path="/home" element={<Home />} />

              <Route path="/colaboradores" element={
                <PermissionRoute canAccess={(user) => permissions.colaborador.admin(user)}>
                  <Colaboradores />
                </PermissionRoute>
              } />

              <Route path="/onboarding" element={<OnboardingRouter />} />

              <Route path="/rh/onboarding" element={
                <RHOnboardingRoute>
                  <RHOnboardingDashboard />
                </RHOnboardingRoute>
              } />

              <Route path="/rh/onboarding/:id" element={
                <RHOnboardingRoute>
                  <RHOnboardingPage />
                </RHOnboardingRoute>
              } />
            </Route>
          </Routes>

          <ToastContainer position="top-right" autoClose={false} />
        </Router>
      </OnboardingProvider>
    </AuthProvider>
  );
}

export default App;