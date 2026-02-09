import React, { useEffect, useState } from "react";
import { admissaoService } from "../../services/admissaoService";
import { Paper, Typography } from "@mui/material";

export default function AdmissaoStatus(): JSX.Element | null {
  const [status, setStatus] = useState<any>(null);
  const [usuario, setUsuario] = useState<any>(null);
  const [colaborador, setColaborador] = useState<any>(null);

  useEffect(() => {
    admissaoService.obterStatus().then(setStatus);
    admissaoService.obterUsuario().then(setUsuario);
    admissaoService.obterColaborador().then(setColaborador);
  }, []);

  if (!status || !usuario || !colaborador) return null;

  return (
    <div className="card">
      <Paper sx={{ p: 2, backgroundColor: "#fff4e0", border: "1px solid #ffa500" }}>
        <strong>Olá {colaborador.nome},</strong>

        <Typography>
          Admissão <strong>{status.step}</strong> nosso RH entrará em contato pelo seu email pessoal <strong>{usuario.emailPessoal}</strong>.
        </Typography>
      </Paper>
    </div>
  );
}
