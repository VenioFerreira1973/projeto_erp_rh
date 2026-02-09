import React, { useEffect, useState } from "react";
import { Box, Paper, Typography } from "@mui/material";
import AdmissaoStatus from "../components/admissao/AdmissaoStatus";
import PerfilResumo from "../components/admissao/PerfilResumo";
import EnderecoResumo from "../components/admissao/EnderecoResumo";
import DadosBancariosResumo from "../components/admissao/DadosBancariosResumo";
import DocumentosResumo from "../components/admissao/DocumentosResumo";
import DocumentosAnexosResumo from "../components/admissao/DocumentosAnexosResumo";
import DependentesResumo from "../components/admissao/DependentesResumo";
import { admissaoService } from "../services/admissaoService";

export default function CandidateHomeEmAnalise({ status }: any): JSX.Element | null {
  const [dadosPessoais, setDadosPessoais] = useState<any>(null);
  const [dependentesData, setDependentesData] = useState<any>(null);
  const [endereco, setEndereco] = useState<any>(null);
  const [dadosBancarios, setDadosBancarios] = useState<any>(null);
  const [documentos, setDocumentos] = useState<any>(null);
  const [documentosAnexos, setDocumentosAnexos] = useState<any>({ documentos: [] });

  const fotoPerfilUrl = documentosAnexos?.documentos?.find((doc: any) => doc.tipoDocumentoAnexo === "FOTO_PERFIL")?.arquivoUrl;

  useEffect(() => {
    admissaoService.obterDadosPessoais().then(setDadosPessoais);
    admissaoService.obterDependentes().then(setDependentesData);
    admissaoService.obterEndereco().then(setEndereco);
    admissaoService.obterDadosBancarios().then(setDadosBancarios);
    admissaoService.obterDocumentos().then(setDocumentos);
    admissaoService.obterDocumentosAnexos().then(setDocumentosAnexos);
  }, []);

  const isStepReprovado = (stepData: any) => stepData?.statusValidacao === "REPROVADO";

  if (!status) return (<Typography sx={{ mt: 2 }}>Carregando status da admissão...</Typography>);
  if (status.step !== "EM_ANALISE") return null;

  return (
    <Box sx={{ mt: 2, width: "100%", maxWidth: "100%" }}>
      <Typography variant="h4">Bem-vindo ao Humanix®</Typography>

      <Paper sx={{ p: 2 }}>
        <AdmissaoStatus />
        {isStepReprovado(dadosPessoais) && (
          <PerfilResumo dadosPessoais={dadosPessoais} fotoUrl={fotoPerfilUrl} statusValidacao={dadosPessoais?.statusValidacao} observacao={dadosPessoais?.observacao || ""} permitirCorrecao={true} onSalvo={() => admissaoService.obterDadosPessoais().then(setDadosPessoais)} />
        )}

        {isStepReprovado(dependentesData) && (
          <DependentesResumo dependentes={dependentesData?.dependentes || []} statusValidacao={dependentesData?.statusValidacao} observacao={dependentesData?.observacao || ""} permitirCorrecao={true} onSalvo={() => admissaoService.obterDependentes().then(setDependentesData)} />
        )}

        {isStepReprovado(endereco) && (
          <EnderecoResumo endereco={endereco} statusValidacao={endereco?.statusValidacao} observacao={endereco?.observacao || ""} permitirCorrecao={true} onSalvo={() => admissaoService.obterEndereco().then(setEndereco)} />
        )}

        {isStepReprovado(dadosBancarios) && (
          <DadosBancariosResumo dadosBancarios={dadosBancarios} statusValidacao={dadosBancarios?.statusValidacao} observacao={dadosBancarios?.observacao || ""} permitirCorrecao={true} onSalvo={() => admissaoService.obterDadosBancarios().then(setDadosBancarios)} />
        )}

        {isStepReprovado(documentos) && (
          <DocumentosResumo documentos={documentos} statusValidacao={documentos?.statusValidacao} observacao={documentos?.observacao || ""} permitirCorrecao={true} onSalvo={() => admissaoService.obterDocumentos().then(setDocumentos)} />
        )}

        {isStepReprovado(documentosAnexos) && (
          <DocumentosAnexosResumo documentosAnexos={documentosAnexos.documentos || []} statusValidacao={documentosAnexos?.statusValidacao} observacao={documentosAnexos?.observacao || ""} permitirCorrecao={true} onSalvo={() => admissaoService.obterDocumentosAnexos().then(setDocumentosAnexos)} />
        )}
      </Paper>
    </Box>
  );
}
