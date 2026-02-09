import React, { useEffect, useState } from "react";
import { Box, Typography, List, ListItem, ListItemButton, ListItemText, CircularProgress } from "@mui/material";
import { rhAdmissaoService } from "../services/rhAdmissaoService";
import { useNavigate } from "react-router-dom";

export default function RHAdmissaoDashboard(): JSX.Element {
  const [candidatos, setCandidatos] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    rhAdmissaoService.listarColaboradoresPendentes().then((res: any) => {
      setCandidatos(res);
      setLoading(false);
    });
  }, []);

  if (loading) return <CircularProgress />;

  const colaboradoresUnicos = Object.values(candidatos.reduce((acc: any, c: any) => { acc[c.colaboradorId] = c; return acc; }, {}));

  return (
    <Box sx={{ mt: 2, width: "100%", maxWidth: "100%" }}>
      <Typography variant="h4" gutterBottom> Candidatos com validações pendentes </Typography>

      <List>
        {colaboradoresUnicos.map((c: any) => (
          <ListItem key={c.colaboradorId} disablePadding>
            <ListItemButton onClick={() => navigate(`/rh/admissao/${c.colaboradorId}`)}>
              <ListItemText primary={`Colaborador ${c.nomeColaborador ?? c.colaboradorId}`} secondary="Possui validações pendentes" />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Box>
  );
}
