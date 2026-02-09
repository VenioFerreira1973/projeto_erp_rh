import api from "../config/api";
import type {
    Validacao,
    DadosPessoais,
    Dependente,
    Endereco,
    DadosBancarios,
    Documento,
} from "./types/rhAdmissao";

const API_URL = "/api/admissao/validacoes";
const API_URL_RH = "api/rh/admissao";

export const rhAdmissaoService = {

    async listarColaboradoresPendentes(): Promise<any[]> {
        const response = await api.get(`${API_URL}/pendentes`);
        return response.data;
    },

    async listarValidacoes(colaboradorId: number): Promise<Validacao[]> {
        const response = await api.get(`${API_URL}/${colaboradorId}/validacoes`);
        return response.data as Validacao[];
    },

    async salvarValidacao(payload: {
        colaboradorId: number;
        step: string;
        status: "APROVADO" | "REPROVADO";
        observacao?: string;
    }): Promise<any> {
        return api.post(`${API_URL}/${payload.colaboradorId}/validar`, {
            colaboradorId: payload.colaboradorId,
            admissaoValidacaoStep: payload.step,
            statusValidacao: payload.status,
            observacao: payload.observacao,
        });
    },

    async salvarValidacoes(payload: {
        colaboradorId: number;
        validacoes: {
            step: string;
            status: "APROVADO" | "REPROVADO";
            observacao?: string;
        }[];
    }) {
        return api.post(`${API_URL}/${payload.colaboradorId}/validar-lote`, payload);
    },

    async obterDadosPessoais(colaboradorId: number): Promise<DadosPessoais> {
        return api.get(`${API_URL_RH}/${colaboradorId}/dados-pessoais`).then((res) => res.data as DadosPessoais);
    },

    async obterDependentes(colaboradorId: number): Promise<Dependente[]> {
        return api.get(`${API_URL_RH}/${colaboradorId}/dependentes`).then((res) => res.data as Dependente[]);
    },

    async obterEndereco(colaboradorId: number): Promise<Endereco> {
        return api.get(`${API_URL_RH}/${colaboradorId}/endereco`).then((res) => res.data as Endereco);
    },

    async obterDadosBancarios(colaboradorId: number): Promise<DadosBancarios> {
        return api.get(`${API_URL_RH}/${colaboradorId}/dados-bancarios`).then((res) => res.data as DadosBancarios);
    },

    async obterDocumentos(colaboradorId: number): Promise<Documento[]> {
        return api.get(`${API_URL_RH}/${colaboradorId}/documentos`).then((res) => res.data as Documento[]);
    },

    async obterDocumentosAnexos(colaboradorId: number): Promise<any[]> {
        const response = await api.get(`${API_URL_RH}/${colaboradorId}/documentos-anexos`);
        return response.data;
    },

    // Métodos com nomes antigos para compatibilidade
    async getDadosPessoais(colaboradorId: number): Promise<DadosPessoais> {
        return this.obterDadosPessoais(colaboradorId);
    },

    async getDependentes(colaboradorId: number): Promise<Dependente[]> {
        return this.obterDependentes(colaboradorId);
    },

    async getEndereco(colaboradorId: number): Promise<Endereco> {
        return this.obterEndereco(colaboradorId);
    },

    async getDadosBancarios(colaboradorId: number): Promise<DadosBancarios> {
        return this.obterDadosBancarios(colaboradorId);
    },

    async getDocumentos(colaboradorId: number): Promise<Documento[]> {
        return this.obterDocumentos(colaboradorId);
    },

    async getDocumentosAnexos(colaboradorId: number): Promise<any[]> {
        return this.obterDocumentosAnexos(colaboradorId);
    },
};

// Manter compatibilidade com nome antigo por enquanto
export const rhOnboardingService = rhAdmissaoService;
