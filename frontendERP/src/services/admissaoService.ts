import api from "../config/api";

const API_URL = "/api/admissao";
const API_URL_ANEXO = "/api/documentos";
const API_URL_VALIDACOES = "/api/admissao/validacoes";

export const admissaoService = {
  async obterStatus() {
    const response = await api.get(`${API_URL}/status`);
    return response.data;
  },

  async obterUsuario() {
    const response = await api.get(`${API_URL}/usuario`);
    return response.data;
  },

  async obterColaborador() {
    const response = await api.get(`${API_URL}/colaborador`);
    return response.data;
  },

  async obterDadosPessoais() {
    const response = await api.get(`${API_URL}/dados-pessoais`);
    return response.data;
  },

  async salvarDadosPessoais(dados: any) {
    const response = await api.post(`${API_URL_VALIDACOES}/dados-pessoais`, dados);
    return response.data;
  },

  async obterDependentes() {
    const response = await api.get(`${API_URL}/dependentes`);
    return response.data;
  },

  async salvarDependentes(dados: any[]) {
    const response = await api.post(`${API_URL_VALIDACOES}/dependentes`,
      dados
    );
    return response.data;
  },

  async obterEndereco() {
    const response = await api.get(`${API_URL}/endereco`);
    return response.data;
  },

  async salvarEndereco(dados: any) {
    const response = await api.post(`${API_URL_VALIDACOES}/endereco`, dados);
    return response.data;
  },

  async obterDadosBancarios() {
    const response = await api.get(`${API_URL}/dados-bancarios`);
    return response.data;
  },

  async salvarDadosBancarios(dados: any) {
    const response = await api.post(`${API_URL_VALIDACOES}/dados-bancarios`, dados);
    return response.data;
  },

  async obterDocumentos() {
    const response = await api.get(`${API_URL}/documentos`);
    return response.data;
  },

  async salvarDocumentos(dados: any) {
    const response = await api.post(`${API_URL_VALIDACOES}/documentos`, dados);
    return response.data;
  },

  async obterDocumentosAnexos() {
    const response = await api.get(`${API_URL}/documentos-anexos`);
    return response.data;
  },

  async salvarDocumentosAnexos(dados: any[]) {
    const response = await api.post(`${API_URL_VALIDACOES}/documentos-anexos`, dados);
    return response.data;
  },

  async carregarDocumentosAnexos(arquivos: File[]): Promise<string[]> {
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
};

// Manter compatibilidade com nome antigo por enquanto
export const onboardingService = admissaoService;
