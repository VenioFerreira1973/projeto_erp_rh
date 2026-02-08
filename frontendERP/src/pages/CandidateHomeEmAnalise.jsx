import { Box, Paper, Typography } from "@mui/material";
import OnboardingStatus from "../components/onboarding/OnboardingStatus";
import PerfilResumo from "../components/onboarding/PerfilResumo";
import EnderecoResumo from "../components/onboarding/EnderecoResumo";
import DadosBancariosResumo from "../components/onboarding/DadosBancariosResumo";
import DocumentosResumo from "../components/onboarding/DocumentosResumo";
import DocumentosAnexosResumo from "../components/onboarding/DocumentosAnexosResumo";
import DependentesResumo from "../components/onboarding/DependentesResumo";
import { useEffect, useState } from "react";
import { onboardingService } from "../services/onboardingService";

export default function CandidateHomeEmAnalise({ status }) {
  const [dadosPessoais, setDadosPessoais] = useState(null);
  const [dependentesData, setDependentesData] = useState(null);
  const [endereco, setEndereco] = useState(null);
  const [dadosBancarios, setDadosBancarios] = useState(null);
  const [documentos, setDocumentos] = useState(null);
  const [documentosAnexos, setDocumentosAnexos] = useState({ documentos: [] });

  const fotoPerfilUrl = documentosAnexos?.documentos?.find(
    (doc) => doc.tipoDocumentoAnexo === "FOTO_PERFIL"
  )?.arquivoUrl;

  useEffect(() => {
    onboardingService.getDadosPessoais().then(setDadosPessoais);
    onboardingService.getDependentes().then(setDependentesData);
    onboardingService.getEndereco().then(setEndereco);
    onboardingService.getDadosBancarios().then(setDadosBancarios);
    onboardingService.getDocumentos().then(setDocumentos);
    onboardingService.getDocumentosAnexos().then(setDocumentosAnexos);
  }, []);

  const isStepReprovado = (stepData) => {
    return stepData?.statusValidacao === "REPROVADO";
  };

  if (!status) {
    return (
      <Typography sx={{ mt: 2 }}>
        Carregando status do onboarding...
      </Typography>
    );
  }

  if (status.step !== "EM_ANALISE") {
    return null;
  }

  return (

    //<Box sx={{ mt: 2, display: "flex", flexDirection: "column", gap: 3 }}>

    <Box sx={{ mt: 2, width: "100%", maxWidth: "100%" }}>
      <Typography variant="h4">
        Bem-vindo ao Humanix®
      </Typography>

      <Paper sx={{ p: 2 }}>
        <OnboardingStatus />
        {isStepReprovado(dadosPessoais) && (
          <PerfilResumo
            dadosPessoais={dadosPessoais}
            fotoUrl={fotoPerfilUrl}
            statusValidacao={dadosPessoais?.statusValidacao}
            observacao={dadosPessoais?.observacao || ""}
            permitirCorrecao={true}
            onSalvo={() => onboardingService.getDadosPessoais().then(setDadosPessoais)}
          />)}

        {isStepReprovado(dependentesData) && (
          <DependentesResumo
            dependentes={dependentesData?.dependentes || []}
            statusValidacao={dependentesData?.statusValidacao}
            observacao={dependentesData?.observacao || ""}
            permitirCorrecao={true}
            onSalvo={() => onboardingService.getDependentes().then(setDependentesData)}
          />)}

        {isStepReprovado(endereco) && (
          <EnderecoResumo
            endereco={endereco}
            statusValidacao={endereco?.statusValidacao}
            observacao={endereco?.observacao || ""}
            permitirCorrecao={true}
            onSalvo={() => onboardingService.getEndereco().then(setEndereco)}
          />)}

        {isStepReprovado(dadosBancarios) && (
          <DadosBancariosResumo
            dadosBancarios={dadosBancarios}
            statusValidacao={dadosBancarios?.statusValidacao}
            observacao={dadosBancarios?.observacao || ""}
            permitirCorrecao={true}
            onSalvo={() => onboardingService.getDadosBancarios().then(setDadosBancarios)}
          />)}

        {isStepReprovado(documentos) && (
          <DocumentosResumo
            documentos={documentos}
            statusValidacao={documentos?.statusValidacao}
            observacao={documentos?.observacao || ""}
            permitirCorrecao={true}
            onSalvo={() => onboardingService.getDocumentos().then(setDocumentos)}
          />)}

        {isStepReprovado(documentosAnexos) && (
          <DocumentosAnexosResumo
            documentosAnexos={documentosAnexos.documentos || []}
            statusValidacao={documentosAnexos?.statusValidacao}
            observacao={documentosAnexos?.observacao || ""}
            permitirCorrecao={true}
            onSalvo={() =>
              onboardingService.getDocumentosAnexos().then(setDocumentosAnexos)
            }
          />)}
      </Paper>

    </Box>
  );
}
