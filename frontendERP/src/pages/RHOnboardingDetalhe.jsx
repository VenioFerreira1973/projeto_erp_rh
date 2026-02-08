import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Box, Button, Typography } from "@mui/material";
import { rhOnboardingService } from "../../services/rhOnboardingService";

import RHValidacaoCard from "./RHValidacaoCard";
import PerfilResumo from "../onboarding/PerfilResumo";
import EnderecoResumo from "../onboarding/EnderecoResumo";
import DadosBancariosResumo from "../onboarding/DadosBancariosResumo";
import DocumentosResumo from "../onboarding/DocumentosResumo";
import DocumentosAnexosResumo from "../onboarding/DocumentosAnexosResumo";

export default function RHOnboardingDetalhe() {
  const { colaboradorId } = useParams();
  const [validacoes, setValidacoes] = useState([]);

  useEffect(() => {
    carregarValidacoes();
  }, []);

  const carregarValidacoes = async () => {
    const data = await rhOnboardingService.listarValidacoes(Number(colaboradorId));
    setValidacoes(data);
  };

  const handleSalvar = async (step, status, observacao) => {
    await rhOnboardingService.validarSecao(Number(colaboradorId), {
      step,
      statusValidacao: status,
      observacao
    });
    carregarValidacoes();
  };

  const tudoAprovado = validacoes.length > 0 &&
    validacoes.every(v => v.statusValidacao === "APROVADO");

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom>
        Validação de Onboarding
      </Typography>

      {/* Resumos (somente leitura) */}
      <PerfilResumo />
      <EnderecoResumo />
      <DadosBancariosResumo />
      <DocumentosResumo />
      <DocumentosAnexosResumo />

      {/* Validações */}
      {validacoes.map((v) => (
        <RHValidacaoCard
          key={v.step}
          titulo={v.step.replace("_", " ")}
          status={v.statusValidacao}
          observacao={v.observacao}
          onSave={(status, obs) =>
            handleSalvar(v.step, status, obs)
          }
        />
      ))}

      {tudoAprovado && (
        <Button
          variant="contained"
          color="success"
          sx={{ mt: 3 }}
          onClick={() =>
            rhOnboardingService.concluirOnboarding(Number(colaboradorId))
          }
        >
          Concluir onboarding
        </Button>
      )}
    </Box>
  );
}
