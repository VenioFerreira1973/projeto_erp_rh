import React, { useEffect, useState, useMemo } from "react";
import { Box, Typography, Button } from "@mui/material";
import { useParams, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import RHValidacaoCard from "../components/homologation/RHValidacaoCard";
import PerfilResumo from "../components/admissao/PerfilResumo";
import EnderecoResumo from "../components/admissao/EnderecoResumo";
import DependentesResumo from "../components/admissao/DependentesResumo";
import DocumentosResumo from "../components/admissao/DocumentosResumo";
import DocumentosAnexosResumo from "../components/admissao/DocumentosAnexosResumo";
import DadosBancariosResumo from "../components/admissao/DadosBancariosResumo";
import { rhAdmissaoService } from "../services/rhAdmissaoService";

export default function RHAdmissaoPage(): JSX.Element {
  const [onboarding, setOnboarding] = useState<any | null>(null);
  const { id: colaboradorId } = useParams();
  const [validacoesLocais, setValidacoesLocais] = useState<any>({});
  const navigate = useNavigate();

  const stepDataFetchers: any = useMemo(() => ({
    DADOS_PESSOAIS: () => rhAdmissaoService.getDadosPessoais(Number(colaboradorId)),
    DEPENDENTES: () => rhAdmissaoService.getDependentes(Number(colaboradorId)),
    ENDERECO: () => rhAdmissaoService.getEndereco(Number(colaboradorId)),
    DADOS_BANCARIOS: () => rhAdmissaoService.getDadosBancarios(Number(colaboradorId)),
    DOCUMENTOS: () => rhAdmissaoService.getDocumentos(Number(colaboradorId)),
    DOCUMENTOS_ANEXOS: () => rhAdmissaoService.getDocumentosAnexos(Number(colaboradorId)),
  }), [colaboradorId]);

  const carregarStep = async (step: string) => {
    if (!step || !stepDataFetchers[step]) return;
    const response = await stepDataFetchers[step]();
    let dadosStep: any = response;
    if (step === "DEPENDENTES") dadosStep = response.dependentes || [];
    setOnboarding((prev: any) => ({ ...prev, dados: { ...prev?.dados, [step]: dadosStep }, validacoes: { ...prev?.validacoes, [step]: { statusValidacao: response.statusValidacao, observacao: response.observacao } } }));
  };

  const shouldShowStep = (step: string) => {
    const status = onboarding.validacoes?.[step]?.statusValidacao;
    return status !== "APROVADO";
  };

  useEffect(() => {
    if (!colaboradorId) return;

    async function carregarOnboarding() {
      const validacoes = await rhAdmissaoService.listarValidacoes(Number(colaboradorId));
      const validacoesMap: any = {};
      const iniciais: any = {};

      validacoes.forEach((v: any) => {
        if (!v.onboardingValidacaoStep) return;
        validacoesMap[v.onboardingValidacaoStep] = v;
        iniciais[v.onboardingValidacaoStep] = { status: v.statusValidacao || "", observacao: v.observacao || "" };
      });

      setValidacoesLocais(iniciais);
      const dados: any = {};

      await Promise.all(validacoes.map(async (v: any) => {
        const step = v.onboardingValidacaoStep;
        const fetcher = stepDataFetchers[step];
        if (!fetcher) return;
        const response = await fetcher();
        dados[step] = step === "DEPENDENTES" ? response.dependentes || [] : response;
      }));

      setOnboarding({ colaboradorId, validacoes: validacoesMap, dados });
    }

    carregarOnboarding();
  }, [colaboradorId, stepDataFetchers]);

  if (!onboarding) return <Typography>Carregando onboarding...</Typography>;

  const handleSalvarTodas = async () => {
    const validacoes: any[] = [];
    for (const step of Object.keys(validacoesLocais)) {
      const { status, observacao } = validacoesLocais[step];
      if (status === "REPROVADO" && !observacao?.trim()) { toast.error("Informe a observação ao reprovar"); return; }
      if (!status) continue;
      validacoes.push({ step, status, observacao });
    }

    await rhAdmissaoService.salvarValidacoes({ colaboradorId: onboarding.colaboradorId, validacoes });
    toast.success("Validações salvas com sucesso", { autoClose: 3000 });
    navigate("/rh/admissao");
  };

  const fotoPerfilUrl = onboarding.dados?.DOCUMENTOS_ANEXOS?.documentos?.find((doc: any) => doc.tipoDocumentoAnexo === "FOTO_PERFIL")?.arquivoUrl;

  return (
    <Box sx={{ mt: 2, width: "100%", maxWidth: "100%" }}>
      <Typography variant="h4" gutterBottom>Validação de Onboarding – RH</Typography>

      {shouldShowStep("DADOS_PESSOAIS") && (
        <RHValidacaoCard status={onboarding.validacoes?.DADOS_PESSOAIS?.statusValidacao} observacao={onboarding.validacoes?.DADOS_PESSOAIS?.observacao} onChange={(status: string, obs: string) => setValidacoesLocais((prev: any) => ({ ...prev, DADOS_PESSOAIS: { status, observacao: obs } }))}>
          <PerfilResumo dadosPessoais={onboarding.dados?.DADOS_PESSOAIS} statusValidacao={onboarding.validacoes?.DADOS_PESSOAIS?.statusValidacao} observacao={onboarding.validacoes?.DADOS_PESSOAIS?.observacao} fotoUrl={fotoPerfilUrl} onSalvo={carregarStep} permitirCorrecao={false} />
        </RHValidacaoCard>
      )}

      {shouldShowStep("DEPENDENTES") && (
        <RHValidacaoCard status={onboarding.validacoes?.DEPENDENTES?.statusValidacao} observacao={onboarding.validacoes?.DEPENDENTES?.observacao} onChange={(status: string, obs: string) => setValidacoesLocais((prev: any) => ({ ...prev, DEPENDENTES: { status, observacao: obs } }))}>
          <DependentesResumo dependentes={onboarding.dados?.DEPENDENTES} statusValidacao={onboarding.validacoes?.DEPENDENTES?.statusValidacao} observacao={onboarding.validacoes?.DEPENDENTES?.observacao} onSalvo={carregarStep} permitirCorrecao={false} />
        </RHValidacaoCard>
      )}

      {shouldShowStep("ENDERECO") && (
        <RHValidacaoCard status={onboarding.validacoes?.ENDERECO?.statusValidacao} observacao={onboarding.validacoes?.ENDERECO?.observacao} onChange={(status: string, obs: string) => setValidacoesLocais((prev: any) => ({ ...prev, ENDERECO: { status, observacao: obs } }))}>
          <EnderecoResumo endereco={onboarding.dados?.ENDERECO} statusValidacao={onboarding.validacoes?.ENDERECO?.statusValidacao} observacao={onboarding.validacoes?.ENDERECO?.observacao} onSalvo={carregarStep} permitirCorrecao={false} />
        </RHValidacaoCard>
      )}

      {shouldShowStep("DADOS_BANCARIOS") && (
        <RHValidacaoCard status={onboarding.validacoes?.DADOS_BANCARIOS?.statusValidacao} observacao={onboarding.validacoes?.DADOS_BANCARIOS?.observacao} onChange={(status: string, obs: string) => setValidacoesLocais((prev: any) => ({ ...prev, DADOS_BANCARIOS: { status, observacao: obs } }))}>
          <DadosBancariosResumo dadosBancarios={onboarding.dados?.DADOS_BANCARIOS} statusValidacao={onboarding.validacoes?.DADOS_BANCARIOS?.statusValidacao} observacao={onboarding.validacoes?.DADOS_BANCARIOS?.observacao} onSalvo={carregarStep} permitirCorrecao={false} />
        </RHValidacaoCard>
      )}

      {shouldShowStep("DOCUMENTOS") && (
        <RHValidacaoCard status={onboarding.validacoes?.DOCUMENTOS?.statusValidacao} observacao={onboarding.validacoes?.DOCUMENTOS?.observacao} onChange={(status: string, obs: string) => setValidacoesLocais((prev: any) => ({ ...prev, DOCUMENTOS: { status, observacao: obs } }))}>
          <DocumentosResumo documentos={onboarding.dados?.DOCUMENTOS} statusValidacao={onboarding.validacoes?.DOCUMENTOS?.statusValidacao} observacao={onboarding.validacoes?.DOCUMENTOS?.observacao} onSalvo={carregarStep} permitirCorrecao={false} />
        </RHValidacaoCard>
      )}

      {shouldShowStep("DOCUMENTOS_ANEXOS") && (
        <RHValidacaoCard status={onboarding.validacoes?.DOCUMENTOS_ANEXOS?.statusValidacao} observacao={onboarding.validacoes?.DOCUMENTOS_ANEXOS?.observacao} onChange={(status: string, obs: string) => setValidacoesLocais((prev: any) => ({ ...prev, DOCUMENTOS_ANEXOS: { status, observacao: obs } }))}>
          <DocumentosAnexosResumo documentosAnexos={onboarding.dados?.DOCUMENTOS_ANEXOS?.documentos ?? []} statusValidacao={onboarding.validacoes?.DOCUMENTOS_ANEXOS?.statusValidacao} observacao={onboarding.validacoes?.DOCUMENTOS_ANEXOS?.observacao} onSalvo={carregarStep} permitirCorrecao={false} />
        </RHValidacaoCard>
      )}

      <Button variant="contained" color="primary" sx={{ mt: 3 }} onClick={handleSalvarTodas}>Salvar validações</Button>
    </Box>
  );
}
