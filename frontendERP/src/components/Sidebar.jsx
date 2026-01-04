import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import { permissions } from "../auth/permissions";

function Sidebar() {
  const navigate = useNavigate();
  const location = useLocation();
  const { user } = useAuth();

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    navigate("/");
  };

  return (
    <div className="sidebar">
      <h3>Menu</h3>

      <ul>
        <li>
          <Link
            to="/home"
            className={location.pathname === "/home" ? "active" : ""}
          >
            Página Inicial
          </Link>
        </li>

        {/* 🔐 Funcionários → somente ADMIN */}
        {permissions.funcionario.admin(user) && (
          <li>
            <Link
              to="/funcionarios"
              className={
                location.pathname === "/funcionarios" ? "active" : ""
              }
            >
              Funcionários
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
