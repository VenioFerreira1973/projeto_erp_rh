import { useEffect, useState } from "react";
import { Box, Typography, List, ListItem, ListItemButton, ListItemText, CircularProgress } from "@mui/material";
import { rhOnboardingService } from "../services/rhOnboardingService";
import { useNavigate } from "react-router-dom";

export default function RHOnboardingDashboard() {
  const [candidatos, setCandidatos] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    rhOnboardingService.listarColaboradoresPendentes().then((res) => {
      setCandidatos(res);
      setLoading(false);
    });
  }, []);

  if (loading) return <CircularProgress />;

  const colaboradoresUnicos = Object.values(
    candidatos.reduce((acc, c) => {
      acc[c.colaboradorId] = c;
      return acc;
    }, {})
  );

  return (
    <Box sx={{ mt: 2, width: "100%", maxWidth: "100%" }}>
      <Typography variant="h4" gutterBottom>
        Candidatos com validações pendentes
      </Typography>

      <List>
        {colaboradoresUnicos.map((c) => (
          <ListItem key={c.colaboradorId} disablePadding>
            <ListItemButton
              onClick={() => navigate(`/rh/onboarding/${c.colaboradorId}`)}
            >
              <ListItemText
                primary={`Colaborador ${c.nomeColaborador ?? c.colaboradorId}`}
                secondary="Possui validações pendentes"
              />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
    </Box>
  );
}
