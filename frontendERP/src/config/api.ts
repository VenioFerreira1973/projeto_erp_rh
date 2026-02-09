import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080",
});

// Interceptor → adiciona o token automaticamente
api.interceptors.request.use((config: any) => {
  const token = localStorage.getItem("token");
  if (token) {
    (config.headers as any).Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
