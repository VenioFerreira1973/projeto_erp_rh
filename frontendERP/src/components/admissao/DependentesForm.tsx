import React, { useState } from "react";
import {
  TextField,
  Button,
  MenuItem,
  Checkbox,
  FormControlLabel,
  Stack,
  Box,
  Paper,
  Typography,
  IconButton,
  Tooltip,
} from "@mui/material";
import { formatarDataBR } from "../../utils/dateUtils";

export default function DependentesForm({ dependentes, setDependentes }: any): JSX.Element {
  const [novo, setNovo] = useState({
    nome: "",
    cpf: "",
    tipoDependente: "",
    dataNascimento: "",
    dependenciaIr: false,
    dependenciaSalarioFamilia: false,
  });

  const onlyNumbers = (value = "") => value.replace(/\D/g, "");

  const maskCPF = (value: string) => {
    return value
      .replace(/\D/g, "")
      .replace(/(\d{3})(\d)/, "$1.$2")
      .replace(/(\d{3})(\d)/, "$1.$2")
      .replace(/(\d{3})(\d{1,2})$/, "$1-$2")
      .slice(0, 14);
  };

  const dependentesOptions = [
    { value: "FILHO", label: "Filho" },
    { value: "CONJUGE", label: "Cônjuge" },
    { value: "OUTRO", label: "Outro" },
  ];

  const adicionar = () => {
    if (!novo.nome || !novo.tipoDependente) return;

    setDependentes((prev: any[]) => [
      ...prev,
      {
        tempId: crypto.randomUUID(),
        ...novo,
      },
    ]);

    setNovo({
      nome: "",
      cpf: "",
      tipoDependente: "",
      dataNascimento: "",
      dependenciaIr: false,
      dependenciaSalarioFamilia: false,
    });
  };

  const remover = (tempId: string) => {
    setDependentes((prev: any[]) => prev.filter((dep: any) => dep.tempId !== tempId));
  };

  return (
    <Paper variant="outlined" sx={{ p: 3, mb: 2 }}>
      <Typography variant="subtitle1" sx={{ mb: 2 }}>
        Dependentes (opcional)
      </Typography>

      {dependentes.map((dep: any) => (
        <Paper key={dep.tempId} variant="outlined" sx={{ p: 1, mb: 0.5, backgroundColor: "grey.50" }}>
          <Stack direction="row" alignItems="center" justifyContent="space-between">
            <Box>
              <strong>{dep.nome}</strong>
              <Box sx={{ fontSize: 13, color: "text.secondary" }}>{dep.tipoDependente}</Box>

              {dep.dataNascimento && (
                <Box sx={{ fontSize: 12, color: "text.secondary" }}>Nascimento: {formatarDataBR(dep.dataNascimento)}</Box>
              )}
            </Box>
            <Tooltip title="Remover" arrow>
              <IconButton size="small" onClick={() => remover(dep.tempId)} sx={{ color: "text.secondary", "&:hover": { color: "error.main" } }}>
                ✕
              </IconButton>
            </Tooltip>
          </Stack>
        </Paper>
      ))}

      <TextField label="Nome do dependente" value={novo.nome} onChange={(e) => setNovo({ ...novo, nome: e.target.value })} fullWidth autoFocus sx={{ mb: 1 }} />

      <TextField name="cpf" label="CPF" value={maskCPF(novo.cpf)} onChange={(e) => setNovo({ ...novo, cpf: onlyNumbers(e.target.value) })} fullWidth sx={{ mb: 1 }} inputProps={{ maxLength: 14 }} />

      <TextField select label="Tipo" name="tipoDependente" value={novo.tipoDependente} onChange={(e) => setNovo({ ...novo, tipoDependente: e.target.value })} fullWidth sx={{ mb: 1 }}>
        {dependentesOptions.map((option) => (
          <MenuItem key={option.value} value={option.value}>
            {option.label}
          </MenuItem>
        ))}
      </TextField>

      <TextField type="date" label="Data de nascimento" InputLabelProps={{ shrink: true }} value={novo.dataNascimento} onChange={(e) => setNovo({ ...novo, dataNascimento: e.target.value })} fullWidth sx={{ mb: 1 }} />

      <FormControlLabel control={<Checkbox checked={novo.dependenciaIr} onChange={(e) => setNovo({ ...novo, dependenciaIr: e.target.checked })} />} label="Dependente para IR" />

      <FormControlLabel control={<Checkbox checked={novo.dependenciaSalarioFamilia} onChange={(e) => setNovo({ ...novo, dependenciaSalarioFamilia: e.target.checked })} />} label="Salário família" />

      <Box sx={{ display: "flex", justifyContent: "flex-end", mt: 1 }}>
        <Button size="small" variant="outlined" onClick={adicionar} disabled={!novo.nome || !novo.tipoDependente} sx={{ textTransform: "none", fontSize: 13, px: 2 }}>
          Adicionar dependente
        </Button>
      </Box>
    </Paper>
  );
}
