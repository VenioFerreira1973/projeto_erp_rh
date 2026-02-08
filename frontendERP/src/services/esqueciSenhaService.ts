import api from "../api";

const esqueciSenhaService = {
  esqueciSenha: async (login: string): Promise<void> => {
    await api.post("/auth/password/forgot", {
      login: login.trim(),
    });
  },
};

export default esqueciSenhaService;
