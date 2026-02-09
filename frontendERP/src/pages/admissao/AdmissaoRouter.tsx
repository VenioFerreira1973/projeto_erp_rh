import React from "react";
import { Navigate } from "react-router-dom";
import { useAdmissao } from "../../context/AdmissaoContext";

import AdmissaoDadosPessoais from "../AdmissaoDadosPessoais";
import AdmissaoEndereco from "../AdmissaoEndereco";
import AdmissaoDadosBancarios from "../AdmissaoDadosBancarios";
import AdmissaoDocumentos from "../AdmissaoDocumentos";
import AdmissaoDocumentosAnexos from "../AdmissaoDocumentosAnexos";
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

export default function AdmissaoRouter(): JSX.Element | null {
  const { status, loading } = useAdmissao() as any;

  if (loading) return <div>Carregando...</div>;
  if (!status) return null;

  if (status?.step === "EM_ANALISE") return <CandidateHomeEmAnalise status={status} />;

  switch (status.step) {
    case ONBOARDING_STEPS.DADOS_PESSOAIS:
      return <AdmissaoDadosPessoais />;
    case ONBOARDING_STEPS.ENDERECO:
      return <AdmissaoEndereco />;
    case ONBOARDING_STEPS.DADOS_BANCARIOS:
      return <AdmissaoDadosBancarios />;
    case ONBOARDING_STEPS.DOCUMENTOS:
      return <AdmissaoDocumentos />;
    case ONBOARDING_STEPS.DOCUMENTOS_ANEXOS:
      return <AdmissaoDocumentosAnexos />;
    case ONBOARDING_STEPS.CONCLUIDO:
      return <Navigate to="/home" replace />;
    default:
      return <Navigate to="/home" replace />;
  }
}
