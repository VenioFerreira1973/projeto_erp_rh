import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import "../App.css"; 

function Login() {
  const [login, setLogin] = useState("");
  const [senha, setSenha] = useState("");
  const { login: authLogin } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await authLogin({ login: login.trim(), senha: senha.trim() });
      navigate("/home");
    } catch (error) {
      console.error("Erro ao logar", error);
      alert("Login inválido");
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h2>Login ERP</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Login</label>
            <input
              type="text"
              value={login}
              onChange={(e) => setLogin(e.target.value)}
            />
          </div>
          <div className="form-group">
            <label>Senha</label>
            <input
              type="password"
              value={senha}
              onChange={(e) => setSenha(e.target.value)}
            />
          </div>
          <button type="submit">Entrar</button>
        </form>
      </div>
    </div>
  );
}

export default Login;
