import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import { permissions } from "../auth/permissions";

function Sidebar() {
  const navigate = useNavigate();
  const location = useLocation();
  const { user, logout, primeiroAcesso } = useAuth();

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

        {/* 🧭 Onboarding */}
        {primeiroAcesso && (
          <li className="onboarding-item">
            <Link
              to="/onboarding"
              className={
                location.pathname.startsWith("/onboarding") ? "active" : ""
              }
            >
              Onboarding
            </Link>
          </li>
        )}

        {permissions.onboarding.write(user) && (
          <li>
            <Link
              to="/rh/onboarding"
              className={location.pathname.startsWith("/rh/onboarding") ? "active" : ""              }
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

export default Sidebar;
