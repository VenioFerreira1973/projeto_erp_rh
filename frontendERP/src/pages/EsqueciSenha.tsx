import { useState } from "react";
import { useLocation } from "react-router-dom";
import esqueciSenhaService from "../services/esqueciSenhaService";

function EsqueciSenha() {
  const location = useLocation();
  const [login, setLogin] = useState(location.state?.login || "");
  const [enviado, setEnviado] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    setLoading(true);

    try {
      await esqueciSenhaService.esqueciSenha(login);
      setEnviado(true);
    } catch (error) {
      console.error(error);
      setEnviado(true); 
    } finally {
      setLoading(false);
    }
  };

  if (enviado) {
    return (
      <div className="login-container">
        <div className="login-card">
          <h2>Verifique seu e-mail</h2>
          <p>
            Se o usuário existir, enviamos um link para redefinição de senha.
          </p>
        </div>
      </div>
    );
  }

  return (
    <div className="login-container">
      <div className="login-card">
        <h2>Esqueci minha senha</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Login</label>
            <input
              type="text"
              value={login}
              onChange={(e) => setLogin(e.target.value)}
              required
            />
          </div>

          <button type="submit" disabled={loading}>
            {loading ? "Enviando..." : "Enviar link"}
          </button>
        </form>
      </div>
    </div>
  );
}

export default EsqueciSenha;
