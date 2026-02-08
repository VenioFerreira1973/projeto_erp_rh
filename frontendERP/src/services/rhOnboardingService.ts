import api from "../api";

const API_URL = "/api/onboarding/validacoes";
const API_URL_RH = "api/rh/onboarding";

export const rhOnboardingService = {

    async listarColaboradoresPendentes() {
        const response = await api.get(`${API_URL}/pendentes`);
        return response.data;
    },

    async listarValidacoes(colaboradorId: number) {
        const response = await api.get(`${API_URL}/${colaboradorId}/validacoes`);
        return response.data;
    },


    async salvarValidacao(payload: {
        colaboradorId: number;
        step: string;
        status: "APROVADO" | "REPROVADO";
        observacao?: string;
    }) {
        return api.post(`${API_URL}/${payload.colaboradorId}/validar`, {
            colaboradorId: payload.colaboradorId,
            onboardingValidacaoStep: payload.step,
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


    async getDadosPessoais(colaboradorId: number) {
        return api.get(`${API_URL_RH}/${colaboradorId}/dados-pessoais`)
            .then(res => res.data);
    },

    async getDependentes(colaboradorId: number) {
        return api.get(`${API_URL_RH}/${colaboradorId}/dependentes`)
            .then(res => res.data);
    },

    async getEndereco(colaboradorId: number) {
        return api.get(`${API_URL_RH}/${colaboradorId}/endereco`)
            .then(res => res.data);
    },

    async getDadosBancarios(colaboradorId: number) {
        return api.get(`${API_URL_RH}/${colaboradorId}/dados-bancarios`)
            .then(res => res.data);
    },

    async getDocumentos(colaboradorId: number) {
        return api.get(`${API_URL_RH}/${colaboradorId}/documentos`)
            .then(res => res.data);
    },

    async getDocumentosAnexos(colaboradorId: number) {
        const response = await api.get(`${API_URL_RH}/${colaboradorId}/documentos-anexos`);
        return response.data;
    },

};