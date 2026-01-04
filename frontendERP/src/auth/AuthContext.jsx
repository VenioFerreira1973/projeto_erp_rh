import { createContext, useContext, useState, useEffect } from "react";
import api from "../api";

const AuthContext = createContext();

const normalizeUser = (usuario) => ({
  login: usuario.login,
  ativo: usuario.ativo,
  permissoes: Array.isArray(usuario.permissoes)
    ? usuario.permissoes.map((p) =>
        typeof p === "string"
          ? { id: null, descricao: p }
          : { id: p.id ?? null, descricao: p.descricao ?? "" }
      )
    : [],
});

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const storedUser = localStorage.getItem("usuario"); // padronizado para "usuario"
    if (storedUser) {
      const parsedUser = JSON.parse(storedUser);
      setUser(normalizeUser(parsedUser));
    }
    setLoading(false);
  }, []);

  const login = async (credentials) => {
    const res = await api.post("/login", credentials);
    const normalized = normalizeUser(res.data.usuarioSecurityResponse);
    localStorage.setItem("token", res.data.token);
    localStorage.setItem("usuario", JSON.stringify(normalized));
    setUser(normalized);

    console.log("Login bem-sucedido, user:", normalized); // debug
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("usuario");
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
