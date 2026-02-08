import { useEffect, useState } from "react";
import { Box, Typography, Button } from "@mui/material";
import { useParams } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useNavigate } from "react-router-dom";

import RHValidacaoCard from "../components/homologation/RHValidacaoCard";
import PerfilResumo from "../components/onboarding/PerfilResumo";
import EnderecoResumo from "../components/onboarding/EnderecoResumo";
import DependentesResumo from "../components/onboarding/DependentesResumo";
import DocumentosResumo from "../components/onboarding/DocumentosResumo";
import DocumentosAnexosResumo from "../components/onboarding/DocumentosAnexosResumo";
import DadosBancariosResumo from "../components/onboarding/DadosBancariosResumo";
import { rhOnboardingService } from "../services/rhOnboardingService";

export default function RHOnboardingPage() {
  const [onboarding, setOnboarding] = useState(null);
  const { id: colaboradorId } = useParams();
  const [validacoesLocais, setValidacoesLocais] = useState({});
  const navigate = useNavigate();

  const stepDataFetchers = {
    DADOS_PESSOAIS: () =>
      rhOnboardingService.getDadosPessoais(colaboradorId),

    DEPENDENTES: () =>
      rhOnboardingService.getDependentes(colaboradorId),

    ENDERECO: () =>
      rhOnboardingService.getEndereco(colaboradorId),

    DADOS_BANCARIOS: () =>
      rhOnboardingService.getDadosBancarios(colaboradorId),

    DOCUMENTOS: () =>
      rhOnboardingService.getDocumentos(colaboradorId),

    DOCUMENTOS_ANEXOS: () =>
      rhOnboardingService.getDocumentosAnexos(colaboradorId),
  };

  const carregarStep = async (step) => {
    if (!step || !stepDataFetchers[step]) return;

    const response = await stepDataFetchers[step]();

    let dadosStep = response;

    if (step === "DEPENDENTES") {
      dadosStep = response.dependentes || [];
    }

    setOnboarding((prev) => ({
      ...prev,
      dados: {
        ...prev.dados,
        [step]: dadosStep,
      },
      validacoes: {
        ...prev.validacoes,
        [step]: {
          statusValidacao: response.statusValidacao,
          observacao: response.observacao,
        },
      },
    }));
  };

  const shouldShowStep = (step) => {
    const status = onboarding.validacoes?.[step]?.statusValidacao;
    return status !== "APROVADO"; // mostra se não está aprovado
  };


  useEffect(() => {
    if (!colaboradorId) return;

    async function carregarOnboarding() {
      const validacoes = await rhOnboardingService.listarValidacoes(colaboradorId);

      const validacoesMap = {};
      const iniciais = {};

      validacoes.forEach((v) => {
        if (!v.onboardingValidacaoStep) return;

        validacoesMap[v.onboardingValidacaoStep] = v;

        iniciais[v.onboardingValidacaoStep] = {
          status: v.statusValidacao || "",
          observacao: v.observacao || "",
        };
      });

      setValidacoesLocais(iniciais);

      const dados = {};

      await Promise.all(
        validacoes.map(async (v) => {
          const step = v.onboardingValidacaoStep;
          const fetcher = stepDataFetchers[step];
          if (!fetcher) return;

          const response = await fetcher(colaboradorId);

          if (step === "DEPENDENTES") {
            dados[step] = response.dependentes || [];
          } else {
            dados[step] = response;
          }
        })
      );

      setOnboarding({
        colaboradorId,
        validacoes: validacoesMap,
        dados,
      });
    }

    carregarOnboarding();
  }, [colaboradorId]);


  if (!onboarding) {
    return <Typography>Carregando onboarding...</Typography>;
  }

  const handleSalvarTodas = async () => {
    const validacoes = [];

    for (const step of Object.keys(validacoesLocais)) {
      const { status, observacao } = validacoesLocais[step];

      if (status === "REPROVADO" && !observacao?.trim()) {
        toast.error("Informe a observação ao reprovar");
        return;
      }

      if (!status) continue;

      validacoes.push({ step, status, observacao });
    }

    await rhOnboardingService.salvarValidacoes({
      colaboradorId: onboarding.colaboradorId,
      validacoes,
    });

    toast.success("Validações salvas com sucesso", { autoClose: 3000, });
    navigate("/rh/onboarding");
  };


  const fotoPerfilUrl =
    onboarding.dados?.DOCUMENTOS_ANEXOS?.documentos?.find(
      (doc) => doc.tipoDocumentoAnexo === "FOTO_PERFIL"
    )?.arquivoUrl;

  return (
    <Box sx={{ mt: 2, width: "100%", maxWidth: "100%", }}>
      <Typography variant="h4" gutterBottom>
        Validação de Onboarding – RH
      </Typography>

      {shouldShowStep("DADOS_PESSOAIS") && (
        <RHValidacaoCard
          status={onboarding.validacoes?.DADOS_PESSOAIS?.statusValidacao}
          observacao={onboarding.validacoes?.DADOS_PESSOAIS?.observacao}
          onChange={(status, obs) =>
            setValidacoesLocais((prev) => ({
              ...prev,
              DADOS_PESSOAIS: { status, observacao: obs },
            }))
          }
        >
          <PerfilResumo
            dadosPessoais={onboarding.dados?.DADOS_PESSOAIS}
            statusValidacao={onboarding.validacoes?.DADOS_PESSOAIS?.statusValidacao}
            observacao={onboarding.validacoes?.DADOS_PESSOAIS?.observacao}
            fotoUrl={fotoPerfilUrl}
            onSalvo={carregarStep}
            permitirCorrecao={false}
          />

        </RHValidacaoCard>
      )}

      {shouldShowStep("DEPENDENTES") && (
        <RHValidacaoCard
          status={onboarding.validacoes?.DEPENDENTES?.statusValidacao}
          observacao={onboarding.validacoes?.DEPENDENTES?.observacao}
          onChange={(status, obs) =>
            setValidacoesLocais((prev) => ({
              ...prev,
              DEPENDENTES: { status, observacao: obs },
            }))
          }
        >
          <DependentesResumo
            dependentes={onboarding.dados?.DEPENDENTES}
            statusValidacao={onboarding.validacoes?.DEPENDENTES?.statusValidacao}
            observacao={onboarding.validacoes?.DEPENDENTES?.observacao}
            onSalvo={carregarStep}
            permitirCorrecao={false}
          />
        </RHValidacaoCard>)}

      {shouldShowStep("ENDERECO") && (
        <RHValidacaoCard
          status={onboarding.validacoes?.ENDERECO?.statusValidacao}
          observacao={onboarding.validacoes?.ENDERECO?.observacao}
          onChange={(status, obs) =>
            setValidacoesLocais((prev) => ({
              ...prev,
              ENDERECO: { status, observacao: obs },
            }))
          }
        >
          <EnderecoResumo
            endereco={onboarding.dados?.ENDERECO}
            statusValidacao={onboarding.validacoes?.ENDERECO?.statusValidacao}
            observacao={onboarding.validacoes?.ENDERECO?.observacao}
            onSalvo={carregarStep}
            permitirCorrecao={false}
          />
        </RHValidacaoCard>
      )}

      {shouldShowStep("DADOS_BANCARIOS") && (
        <RHValidacaoCard
          status={onboarding.validacoes?.DADOS_BANCARIOS?.statusValidacao}
          observacao={onboarding.validacoes?.DADOS_BANCARIOS?.observacao}
          onChange={(status, obs) =>
            setValidacoesLocais((prev) => ({
              ...prev,
              DADOS_BANCARIOS: { status, observacao: obs },
            }))
          }

        >
          <DadosBancariosResumo
            dadosBancarios={onboarding.dados?.DADOS_BANCARIOS}
            statusValidacao={onboarding.validacoes?.DADOS_BANCARIOS?.statusValidacao}
            observacao={onboarding.validacoes?.DADOS_BANCARIOS?.observacao}
            onSalvo={carregarStep}
            permitirCorrecao={false}
          />
        </RHValidacaoCard>)}

      {shouldShowStep("DOCUMENTOS") && (
        <RHValidacaoCard
          status={onboarding.validacoes?.DOCUMENTOS?.statusValidacao}
          observacao={onboarding.validacoes?.DOCUMENTOS?.observacao}
          onChange={(status, obs) =>
            setValidacoesLocais((prev) => ({
              ...prev,
              DOCUMENTOS: { status, observacao: obs },
            }))
          }

        >
          <DocumentosResumo
            documentos={onboarding.dados?.DOCUMENTOS}
            statusValidacao={onboarding.validacoes?.DOCUMENTOS?.statusValidacao}
            observacao={onboarding.validacoes?.DOCUMENTOS?.observacao}
            onSalvo={carregarStep}
            permitirCorrecao={false}
          />
        </RHValidacaoCard>)}

      {shouldShowStep("DOCUMENTOS_ANEXOS") && (
        <RHValidacaoCard
          status={onboarding.validacoes?.DOCUMENTOS_ANEXOS?.statusValidacao}
          observacao={onboarding.validacoes?.DOCUMENTOS_ANEXOS?.observacao}
          onChange={(status, obs) =>
            setValidacoesLocais((prev) => ({
              ...prev,
              DOCUMENTOS_ANEXOS: { status, observacao: obs },
            }))
          }
        >
          <DocumentosAnexosResumo
            documentosAnexos={onboarding.dados?.DOCUMENTOS_ANEXOS?.documentos ?? []}
            statusValidacao={onboarding.validacoes?.DOCUMENTOS_ANEXOS?.statusValidacao}
            observacao={onboarding.validacoes?.DOCUMENTOS_ANEXOS?.observacao}
            onSalvo={carregarStep}
            permitirCorrecao={false}
          />

        </RHValidacaoCard>)}

      <Button
        variant="contained"
        color="primary"
        sx={{ mt: 3 }}
        onClick={handleSalvarTodas}
      >
        Salvar validações
      </Button>
    </Box>
  );
}
