import React from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../../../auth/AuthContext";
import { permissions } from "../../../auth/permissions";

export default function Sidebar(): JSX.Element {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout, primeiroAcesso } = useAuth() as any;

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <div className="sidebar">
      <h3>Menu</h3>

      <ul className="sidebar-menu">
        {!primeiroAcesso && (
          <li>
            <Link
              to="/home"
              className={location.pathname === "/home" ? "active" : ""}
            >
              Página Inicial
            </Link>
          </li>
        )}

        {permissions.colaborador.admin(user) && (
          <li>
            <Link
              to="/colaboradores"
              className={
                location.pathname === "/colaboradores" ? "active" : ""
              }
            >
              Candidato
            </Link>
          </li>
        )}

        {primeiroAcesso && (
          <li className="admissao-item">
            <Link
              to="/admissao"
              className={
                location.pathname.startsWith("/admissao") ? "active" : ""
              }
            >
              Admissão
            </Link>
          </li>
        )}

        {permissions.admissao.write(user) && (
          <li>
            <Link
              to="/rh/admissao"
              className={location.pathname.startsWith("/rh/admissao") ? "active" : ""}
            >
              Validação RH
            </Link>
          </li>
        )}
      </ul>

      <button className="logout" onClick={handleLogout}>
        Logout
      </button>
    </div>
  );
}
