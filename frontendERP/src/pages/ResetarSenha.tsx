import React, { useState, useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import resetarSenhaService from "../services/resetarSenhaService";

export default function ResetarSenha(): JSX.Element {
  const [params] = useSearchParams();
  const navigate = useNavigate();
  const token = params.get("token");

  const [novaSenha, setNovaSenha] = useState("");
  const [confirmarSenha, setConfirmarSenha] = useState("");
  const [valido, setValido] = useState(false);
  const [erro, setErro] = useState("");
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!token) { setErro("Token inválido"); setLoading(false); return; }
    resetarSenhaService.validarToken(token).then(() => setValido(true)).catch(() => setErro("Link expirado ou inválido")).finally(() => setLoading(false));
  }, [token]);

  const handleSubmit = async (e: any) => {
    e.preventDefault();
    if (novaSenha !== confirmarSenha) { setErro("As senhas não coincidem"); return; }
    try {
      await resetarSenhaService.resetarSenha({ token: token!, novaSenha });
      alert("Senha redefinida com sucesso");
      navigate("/");
    } catch (_error) {
      void _error;
      setErro("Erro ao redefinir senha");
    }
  };

  if (loading) return <p>Validando link...</p>;
  if (!valido) return <p>{erro}</p>;

  return (
    <div className="login-container">
      <div className="login-card">
        <h2>Redefinir senha</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Nova senha</label>
            <input type="password" value={novaSenha} onChange={(e) => setNovaSenha(e.target.value)} required />
          </div>

          <div className="form-group">
            <label>Confirmar senha</label>
            <input type="password" value={confirmarSenha} onChange={(e) => setConfirmarSenha(e.target.value)} required />
          </div>

          {erro && <p style={{ color: "red" }}>{erro}</p>}

          <button type="submit">Redefinir senha</button>
        </form>
      </div>
    </div>
  );
}
