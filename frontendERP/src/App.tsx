import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import { AuthProvider } from "./auth/AuthContext";
import PrivateRoute from "./routes/PrivateRoute";
import PermissionRoute from "./routes/PermissionRoute";
import { permissions } from "./auth/permissions";
import { AdmissaoProvider } from "./context/AdmissaoContext";
import RootRedirect from "./routes/RootRedirect";
import AppLayout from "./components/common/AppLayout";

import Login from "./pages/Login";
import Home from "./pages/Home";
import Colaboradores from "./pages/Colaboradores";
import PrimeiroAcesso from "./pages/PrimeiroAcesso";
import EsqueciSenha from "./pages/EsqueciSenha";
import ResetarSenha from "./pages/ResetarSenha";
import AdmissaoRouter from "./pages/admissao/AdmissaoRouter";
import RHAdmissaoRoute from "./routes/RHAdmissaoRoute";
import RHAdmissaoPage from "./pages/RHAdmissaoPage";
import RHAdmissaoDashboard from "./pages/RHAdmissaoDashboard";

import './App.css';

function App() {
  return (
    <AuthProvider>
      <AdmissaoProvider>
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
                <PermissionRoute canAccess={(user:any) => permissions.colaborador.admin(user)}>
                  <Colaboradores />
                </PermissionRoute>
              } />

              <Route path="/admissao" element={<AdmissaoRouter />} />

              <Route path="/rh/admissao" element={
                <RHAdmissaoRoute>
                  <RHAdmissaoDashboard />
                </RHAdmissaoRoute>
              } />

              <Route path="/rh/admissao/:id" element={
                <RHAdmissaoRoute>
                  <RHAdmissaoPage />
                </RHAdmissaoRoute>
              } />
            </Route>
          </Routes>

          <ToastContainer position="top-right" autoClose={false} />
        </Router>
      </AdmissaoProvider>
    </AuthProvider>
  );
}

export default App;
