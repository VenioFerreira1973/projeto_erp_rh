import { TextField, Button, Checkbox, FormControlLabel, MenuItem } from "@mui/material";
import { useEffect, useState } from "react";
import { onboardingCorrigirService } from "../../services/onboardingCorrigirService";
import { formatarDataBR } from "../../utils/dateUtils";

export default function DependentesResumo({
  dependentes,
  statusValidacao,
  observacao,
  onSalvo,
  permitirCorrecao
}) {
  const podeEditar = permitirCorrecao && statusValidacao === "REPROVADO";
  const [editando, setEditando] = useState(false);
  const [form, setForm] = useState([]);

  useEffect(() => {
    if (Array.isArray(dependentes)) {
      setForm(
        dependentes.map((dep) => ({
          ...dep,
          nome: dep.nome ?? "",
          cpf: dep.cpf ?? "",
          dataNascimento: dep.dataNascimento ?? "",
          dependenciaIr: !!dep.dependenciaIr,
          dependenciaSalarioFamilia: !!dep.dependenciaSalarioFamilia,
        }))
      );
    } else {
      setForm([]);
    }
  }, [dependentes]);

  if (form.length === 0) {
    return <p>Nenhum dependente encontrado.</p>;
  }

  const handleChange = (index, campo) => (event) => {
    const value =
      event.target.type === "checkbox"
        ? event.target.checked
        : event.target.value;

    setForm((prev) =>
      prev.map((dep, i) =>
        i === index ? { ...dep, [campo]: value } : dep
      )
    );
  };

  const handleSalvar = async () => {
    await onboardingCorrigirService.corrigirDependentes(form);
    setEditando(false);
    onSalvo("DEPENDENTES");
  };

  const dependentesOptions = [
    { value: "FILHO", label: "Filho" },
    { value: "CONJUGE", label: "Cônjuge" },
    { value: "OUTRO", label: "Outro" },
  ];

  return (
    <div className="card">
      <h3>Dependentes</h3>

      {podeEditar && observacao && (
        <div style={{ background: "#fdecea", padding: 12, marginBottom: 16 }}>
          <strong>Observação do RH:</strong>
          <p>{observacao}</p>
        </div>
      )}

      {form.map((dep, index) => (
        <div
          key={dep.id}
          style={{
            marginBottom: 16,
            paddingBottom: 16,
            borderBottom: "1px solid #eee",
          }}
        >
          <TextField
            label="Nome do dependente"
            value={dep.nome ?? ""}
            onChange={handleChange(index, "nome")}
            disabled={!editando}
            fullWidth
            autoFocus
            margin="dense"
          />

          <TextField
            select
            label="Tipo do dependente"
            value={dep.tipoDependente ?? ""}
            onChange={handleChange(index, "tipoDependente")}
            disabled={!editando}
            fullWidth
            margin="dense"
          >
            {dependentesOptions.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                {option.label}
              </MenuItem>
            ))}
          </TextField>

          <TextField
            label="CPF"
            value={dep.cpf ?? ""}
            onChange={handleChange(index, "cpf")}
            disabled={!editando}
            fullWidth
            margin="dense"
          />

          {editando ? (
            <TextField
              label="Data de nascimento"
              type="date"
              value={dep.dataNascimento ?? ""}
              onChange={handleChange(index, "dataNascimento")}
              fullWidth
              margin="dense"
              InputLabelProps={{ shrink: true }}
            />
          ) : (
            <TextField
              label="Data de nascimento"
              value={formatarDataBR(dep.dataNascimento)}
              disabled
              fullWidth
              margin="dense"
            />
          )}

          <FormControlLabel
            control={
              <Checkbox
                checked={dep.dependenciaIr}
                onChange={handleChange(index, "dependenciaIr")}
                disabled={!editando}
              />
            }
            label="Dependente para IR"
          />

          <FormControlLabel
            control={
              <Checkbox
                checked={dep.dependenciaSalarioFamilia}
                onChange={handleChange(index, "dependenciaSalarioFamilia")}
                disabled={!editando}
              />
            }
            label="Salário família"
          />
        </div>
      ))}

      {podeEditar && (
        <Button
          sx={{ mt: 2 }}
          variant={editando ? "contained" : "outlined"}
          color="warning"
          onClick={editando ? handleSalvar : () => setEditando(true)}
        >
          {editando ? "Salvar correção" : "Corrigir dados"}
        </Button>
      )}
    </div>
  );
}
