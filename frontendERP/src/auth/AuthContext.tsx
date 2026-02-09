import type { ReactNode } from "react";
import { createContext, useContext, useState, useEffect } from "react";
import api from "../config/api";
import { jwtDecode } from "jwt-decode";

const AuthContext = createContext<any>(null);

const UsuarioStatus = {
  ATIVO: "ATIVO",
  INATIVO: "INATIVO",
};

const normalizeUser = (usuario: any) => ({
  login: usuario.login,
  status: usuario.status,
  estaAtivo: usuario.status === UsuarioStatus.ATIVO,
  permissoes: Array.isArray(usuario.permissoes)
    ? usuario.permissoes.map((p: any) =>
        typeof p === "string"
          ? { id: null, descricao: p }
          : { id: p.id ?? null, descricao: p.descricao ?? "" }
      )
    : [],
});

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<any>(null);
  const [primeiroAcesso, setPrimeiroAcesso] = useState(false);
  const [loading, setLoading] = useState(true);
  const [token, setToken] = useState<string | null>(null);

  useEffect(() => {
    if (token) {
      api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    } else {
      delete api.defaults.headers.common["Authorization"];
    }
  }, [token]);

  useEffect(() => {
    const storedToken = localStorage.getItem("token");
    const storedUser = localStorage.getItem("usuario");
    const storedPrimeiroAcesso = localStorage.getItem("primeiroAcesso");

    if (storedToken) setToken(storedToken);
    if (storedUser) setUser(normalizeUser(JSON.parse(storedUser)));
    if (storedPrimeiroAcesso !== null)
      setPrimeiroAcesso(storedPrimeiroAcesso === "true");

    setLoading(false);
  }, []);

  const login = async (credentials: any) => {
    const res = await api.post("/login", credentials);
    const { token: novoToken, usuarioSecurityResponse, primeiroAcesso: pa } = res.data;

    const normalizedUser = normalizeUser(usuarioSecurityResponse);

    localStorage.setItem("token", novoToken);
    localStorage.setItem("usuario", JSON.stringify(normalizedUser));
    localStorage.setItem("primeiroAcesso", pa);

    setUser(normalizedUser);
    setPrimeiroAcesso(pa);
    setToken(novoToken);

    return res;
  };

  const logout = () => {
    localStorage.clear();
    setUser(null);
    setPrimeiroAcesso(false);
    setToken(null);
  };

  const atualizarToken = (novoToken: string) => {
    try {
      const decoded: any = jwtDecode(novoToken);
      
      setToken(novoToken);
      setPrimeiroAcesso(decoded.primeiroAcesso || false);

      const novoUser = {
        login: decoded.sub,
        status: UsuarioStatus.ATIVO,
        estaAtivo: true,
        permissoes: decoded.permissoes ?? [],
      };

      localStorage.setItem("token", novoToken);
      localStorage.setItem("usuario", JSON.stringify(novoUser));
      localStorage.setItem("primeiroAcesso", decoded.primeiroAcesso || false);

      setUser(novoUser);
    } catch (err) {
      console.error("Erro ao atualizar token:", err);
    }
  };

  const finalizarPrimeiroAcesso = () => {
    setPrimeiroAcesso(false);
    localStorage.setItem("primeiroAcesso", "false");
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        primeiroAcesso,
        loading,
        login,
        logout,
        atualizarToken,
        finalizarPrimeiroAcesso,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
