import api from "../api";

const API_URL = "/api/onboarding";
const API_URL_ANEXO = "/api/documentos";
const API_URL_VALIDACOES = "/api/onboarding/validacoes";

export const onboardingService = {
  async getStatus() {
    const response = await api.get(`${API_URL}/status`);
    return response.data;
  },

  async getUsuario() {
    const response = await api.get(`${API_URL}/usuario`);
    return response.data;
  },

  async getColaborador() {
    const response = await api.get(`${API_URL}/colaborador`);
    return response.data;
  },

  async getDadosPessoais() {
    const response = await api.get(`${API_URL}/dados-pessoais`);
    return response.data;
  },

  async salvarDadosPessoais(dados: any) {
    const response = await api.post(`${API_URL_VALIDACOES}/dados-pessoais`, dados);
    return response.data;
  },


  async getDependentes() {
    const response = await api.get(`${API_URL}/dependentes`);
    return response.data;
  },

  async salvarDependentes(dados: any[]) {
    const response = await api.post(`${API_URL_VALIDACOES}/dependentes`,
      dados
    );
    return response.data;
  },

  async getEndereco() {
    const response = await api.get(`${API_URL}/endereco`);
    return response.data;
  },

  async salvarEndereco(dados: any) {
    const response = await api.post(`${API_URL_VALIDACOES}/endereco`, dados);
    return response.data;
  },

  async getDadosBancarios() {
    const response = await api.get(`${API_URL}/dados-bancarios`);
    return response.data;
  },

  async salvarDadosBancarios(dados: any) {
    const response = await api.post(`${API_URL_VALIDACOES}/dados-bancarios`, dados);
    return response.data;
  },

  async getDocumentos() {
    const response = await api.get(`${API_URL}/documentos`);
    return response.data;
  },

  async salvarDocumentos(dados: any) {
    const response = await api.post(`${API_URL_VALIDACOES}/documentos`, dados);
    return response.data;
  },

  async getDocumentosAnexos() {
    const response = await api.get(`${API_URL}/documentos-anexos`);
    return response.data;
  },

  async salvarDocumentosAnexos(dados: any[]) {
    const response = await api.post(`${API_URL_VALIDACOES}/documentos-anexos`, dados);
    return response.data;
  },

  async uploadDocumentosAnexos(arquivos: File[]): Promise<string[]> {
    const formData = new FormData();
    arquivos.forEach((file: File) => {
      formData.append("arquivos", file);
    });

    const response = await api.post(
      `${API_URL_ANEXO}/anexos/upload`,
      formData,
      {
        headers: { "Content-Type": "multipart/form-data" },
      }
    );

    return response.data;
  },

  async enviarParaAnalise() {
    const response = await api.post(`${API_URL}/analisar`);
    return response.data;
  },

  async concluirOnboarding() {
    const response = await api.post(`${API_URL}/concluir`);
    return response.data;
  },
};