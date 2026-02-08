import { Navigate } from "react-router-dom";
import { useOnboarding } from "../../context/OnboardingContext";

import OnboardingDadosPessoais from "../OnboardingDadosPessoais";
import OnboardingEndereco from "../OnboardingEndereco";
import OnboardingDadosBancarios from "../OnboardingDadosBancarios";
import OnboardingDocumentos from "../OnboardingDocumentos";
import OnboardingDocumentosAnexos from "../OnboardingDocumentosAnexos";
import CandidateHomeEmAnalise from "../CandidateHomeEmAnalise";

export const ONBOARDING_STEPS = {
    DADOS_PESSOAIS: "DADOS_PESSOAIS",
    ENDERECO: "ENDERECO",
    DADOS_BANCARIOS: "DADOS_BANCARIOS",
    DOCUMENTOS: "DOCUMENTOS",
    DOCUMENTOS_ANEXOS: "DOCUMENTOS_ANEXOS",
    EM_ANALISE: "EM_ANALISE",
    CONCLUIDO: "CONCLUIDO",
};

export default function OnboardingRouter() {
    const { status, loading } = useOnboarding();

    if (loading) return <div>Carregando...</div>;
    if (!status) return null;

    if (status?.step === "EM_ANALISE") {
        return <CandidateHomeEmAnalise status={status} />;
    }

    switch (status.step) {
        case ONBOARDING_STEPS.DADOS_PESSOAIS:
            return <OnboardingDadosPessoais />;
        case ONBOARDING_STEPS.ENDERECO:
            return <OnboardingEndereco />;
        case ONBOARDING_STEPS.DADOS_BANCARIOS:
            return <OnboardingDadosBancarios />;
        case ONBOARDING_STEPS.DOCUMENTOS:
            return <OnboardingDocumentos />;
        case ONBOARDING_STEPS.DOCUMENTOS_ANEXOS:
            return <OnboardingDocumentosAnexos />;
        case ONBOARDING_STEPS.CONCLUIDO:
            return <Navigate to="/home" replace />;
        default:
            return <Navigate to="/home" replace />;
    }

}
