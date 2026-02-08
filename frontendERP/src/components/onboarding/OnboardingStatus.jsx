import { useEffect, useState } from "react";
import { onboardingService } from "../../services/onboardingService";
import { Paper, Typography } from "@mui/material";

export default function OnboardingStatus() {
  const [status, setStatus] = useState(null);
  const [usuario, setUsuario] = useState(null);
  const [colaborador, setColaborador] = useState(null);

  useEffect(() => {
    onboardingService.getStatus().then(setStatus);
    onboardingService.getUsuario().then(setUsuario);
    onboardingService.getColaborador().then(setColaborador);
  }, []);

  if (!status || !usuario || !colaborador) return null;

  return (

    <div className="card">
      <Paper
        sx={{
          p: 2,
          backgroundColor: "#fff4e0",
          border: "1px solid #ffa500",
        }}
      >
        <strong>Olá {colaborador.nome},</strong>

        <Typography>
          Onboarding <strong>{status.step}</strong> nosso RH entrará em contato 
          pelo seu email pessoal <strong>{usuario.emailPessoal}</strong>.
        </Typography>
      </Paper>
    </div>
    
  );
}
