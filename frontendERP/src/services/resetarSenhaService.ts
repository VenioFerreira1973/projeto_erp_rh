import api from "../api";

export interface ResetarSenhaParams {
  token: string;
  novaSenha: string;
}

export const validarToken = async (token: string): Promise<void> => {
  await api.get("/auth/password/validate", {
    params: { token },
  });
};

export const resetarSenha = async (params: ResetarSenhaParams): Promise<void> => {
  await api.post("/auth/password/reset", {
    token: params.token,
    novaSenha: params.novaSenha,
  });
};

export default {
  validarToken,
  resetarSenha,
};
