import { useState } from "react";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useAuth } from "../auth/AuthContext";
import { useNavigate } from "react-router-dom";
import primeiroAcessoService from "../services/primeiroAcessoService";

function PrimeiroAcesso() {
  const [senha, setSenha] = useState("");
  const [loading, setLoading] = useState(false);
  const [mostrarSenha, setMostrarSenha] = useState(false);
  const { atualizarToken } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (loading) return;
    setLoading(true);

    try {
      const res = await primeiroAcessoService.finalizarPrimeiroAcesso({
        novaSenha: senha,
      });

      atualizarToken(res.token);
      navigate("/onboarding");
    } catch (err) {
      const mensagemErro = err.response?.data?.message || err.message ||
        "Erro inesperado ao finalizar o primeiro acesso";

      toast.error(mensagemErro, { autoClose: 3000, });

      if (mensagemErro.includes("diferente")) {
        setSenha("");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h2>
          Seja bem-vindo <br />
          <small>Este é o seu primeiro acesso ao sistema</small>
        </h2>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Cadastre sua nova senha</label>

            <div className="password-wrapper">
              <input
                type={mostrarSenha ? "text" : "password"}
                value={senha}
                onChange={(e) => setSenha(e.target.value)}
                autoFocus
              />

              <span
                className="password-toggle"
                onClick={() => setMostrarSenha(!mostrarSenha)}
                title={mostrarSenha ? "Ocultar senha" : "Mostrar senha"}
              >
                {mostrarSenha ? (
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="20"
                    height="20"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M13.875 18.825A10.05 10.05 0 0112 19c-5.523 0-10-4.477-10-10 0-1.02.153-2.003.438-2.93M9.88 9.88a3 3 0 104.24 4.24M6.1 6.1L18 18"
                    />
                  </svg>
                ) : (
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="20"
                    height="20"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M1.5 12s4.5-7.5 10.5-7.5S22.5 12 22.5 12 18 19.5 12 19.5 1.5 12 1.5 12z"
                    />
                    <circle cx="12" cy="12" r="3" />
                  </svg>
                )}
              </span>
            </div>
          </div>

          <button type="submit" disabled={loading}>
            {loading ? "Salvando..." : "Continuar"}
          </button>
        </form>
      </div>
    </div>
  );
}

export default PrimeiroAcesso;
