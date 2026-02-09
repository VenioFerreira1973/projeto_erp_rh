import api from "../config/api";

export interface PrimeiroAcessoParams {
  novaSenha: string;
}

export interface PrimeiroAcessoResponse {
  token: string;
}

const finalizarPrimeiroAcesso = async (
  params: PrimeiroAcessoParams
): Promise<PrimeiroAcessoResponse> => {
  const response = await api.post("/primeiro-acesso/finalizar", {
    novaSenha: params.novaSenha,
  });
  return response.data;
};

export default { finalizarPrimeiroAcesso };
