import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import "../App.css";
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import { Box, Paper, Typography } from '@mui/material';
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export default function Login() {
  const [loginInput, setLoginInput] = useState("");
  const [senha, setSenha] = useState("");
  const [mostrarSenha, setMostrarSenha] = useState(false);

  const { login, user, primeiroAcesso, loading } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!loading && user) {
      navigate(primeiroAcesso ? "/primeiro-acesso" : "/home");
    }
  }, [user, primeiroAcesso, loading, navigate]);

  const handleSubmit = async (e: any) => {
    e.preventDefault();

    try {
      const res = await login({
        login: loginInput.trim(),
        senha: senha.trim(),
      });

      const { primeiroAcesso: pa } = res.data;

      navigate(pa ? "/primeiro-acesso" : "/home");

    } catch (error) {
      console.error("Erro na requisição:", error);
      toast.error("Erro ao logar. Verifique usuário e senha.", { autoClose: 3000 });
    }
  };

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        bgcolor: 'background.default',
      }}
    >
      <Paper
        elevation={3}
        sx={{
          p: 4,
          width: 360,
          borderRadius: 2,
        }}
      >
        <Typography variant="h5" align="center" gutterBottom>
          Humanix®
        </Typography>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <TextField
              label="Login"
              variant="outlined"
              fullWidth
              autoFocus
              value={loginInput}
              onChange={(e) => setLoginInput(e.target.value)}
            />
          </div>

          <div className="form-group">
            <label>Senha</label>
            <div className="password-wrapper">
              <TextField
                label="Senha"
                type={mostrarSenha ? "text" : "password"}
                variant="outlined"
                fullWidth
                value={senha}
                onChange={(e) => setSenha(e.target.value)}
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

          <Link
            to="/esqueci-senha"
            state={{ login: loginInput.trim() }}
            className="forgot-link"
          >
            Esqueci minha senha
          </Link>

          <Button
            type="submit"
            variant="contained"
            color="primary"
            fullWidth
            sx={{ mt: 2 }}
          >
            Entrar
          </Button>
        </form>
      </Paper>
    </Box>
  );
}
