import React, { useEffect, useState, useCallback } from "react";
import { useParams } from "react-router-dom";
import { Box, Button, Typography } from "@mui/material";
import { rhAdmissaoService } from "../services/rhAdmissaoService";

import RHValidacaoCard from "../components/homologation/RHValidacaoCard";

export default function RHAdmissaoDetalhe(): JSX.Element {
  const { colaboradorId } = useParams();
  const [validacoes, setValidacoes] = useState<any[]>([]);

    const carregarValidacoes = useCallback(async () => {
      const data = await rhAdmissaoService.listarValidacoes(Number(colaboradorId));
      setValidacoes(data);
    }, [colaboradorId]);

    useEffect(() => {
      if (colaboradorId) carregarValidacoes();
    }, [carregarValidacoes, colaboradorId]);

  const handleSalvar = async (step: string, status: string, observacao: string) => {
    await rhAdmissaoService.salvarValidacao({ colaboradorId: Number(colaboradorId), step, status: status as "APROVADO" | "REPROVADO", observacao });
    carregarValidacoes();
  };

  const tudoAprovado = validacoes.length > 0 && validacoes.every((v) => v.statusValidacao === "APROVADO");

  return (
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom>Validação de Admissão</Typography>

      {validacoes.map((v) => (
        <RHValidacaoCard key={v.step} titulo={v.step.replace("_", " ")} status={v.statusValidacao} observacao={v.observacao} onChange={(status: string, obs: string) => handleSalvar(v.step, status, obs)} />
      ))}

      {tudoAprovado && (
        <Button variant="contained" color="success" sx={{ mt: 3 }}>Todos os itens aprovados!</Button>
      )}
    </Box>
  );
}
