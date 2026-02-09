import api from "../config/api";

const API_URL_CORRIGIR = "/api/admissao/corrigir";

export const admissaoCorrigirService = {

    async corrigirDadosPessoais(dados: any) {
        const response = await api.put(
            `${API_URL_CORRIGIR}/dados-pessoais`,
            dados
        );
        return response.data;
    },

    async corrigirDependentes(dados: any) {
        const response = await api.put(
            `${API_URL_CORRIGIR}/dependentes`,
            dados
        );
        return response.data;
    },

    async corrigirDadosBancarios(dados: any) {
        const response = await api.put(
            `${API_URL_CORRIGIR}/dados-bancarios`,
            dados
        );
        return response.data;
    },

    async corrigirDocumentos(dados: any) {
        const response = await api.put(
            `${API_URL_CORRIGIR}/documentos`,
            dados
        );
        return response.data;
    },

    async corrigirEndereco(dados: any) {
        const response = await api.put(
            `${API_URL_CORRIGIR}/endereco`,
            dados
        );
        return response.data;
    },

    async corrigirDocumentosAnexos(formData: any) {
        const response = await api.put(
            `${API_URL_CORRIGIR}/documentos-anexos`,
            formData,
            {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
            }
        );

        return response.data;
    }
}

// Manter compatibilidade com nome antigo por enquanto
export const onboardingCorrigirService = admissaoCorrigirService;
