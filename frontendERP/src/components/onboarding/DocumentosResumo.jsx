import {
  TextField,
  Button,
  Checkbox,
  FormControlLabel,
} from "@mui/material";
import { useEffect, useState } from "react";
import { onboardingCorrigirService } from "../../services/onboardingCorrigirService";

export default function DocumentosResumo({
  documentos,
  statusValidacao,
  observacao,
  onSalvo,
  permitirCorrecao
}) {
  const podeEditar = permitirCorrecao && statusValidacao === "REPROVADO";
  const [editando, setEditando] = useState(false);
  const [form, setForm] = useState(documentos);

  const onlyNumbers = (value = "") => value.replace(/\D/g, "");

  const maskCPF = (value) => {
    return value
      .replace(/\D/g, "")
      .replace(/(\d{3})(\d)/, "$1.$2")
      .replace(/(\d{3})(\d)/, "$1.$2")
      .replace(/(\d{3})(\d{1,2})$/, "$1-$2")
      .slice(0, 14);
  };

  const maskPisPasep = (value = "") => {
    return value
      .replace(/\D/g, "")
      .replace(/(\d{3})(\d)/, "$1.$2")
      .replace(/(\d{5})(\d)/, "$1.$2")
      .replace(/(\d{2})(\d{1})$/, "$1-$2")
      .slice(0, 14);
  };

  useEffect(() => {
    setForm(documentos);
  }, [documentos]);

  if (!form) {
    return <p>Nenhum documento encontrado.</p>;
  }

  const handleChange = (campo) => (event) => {
    const value =
      event.target.type === "checkbox"
        ? event.target.checked
        : event.target.value;

    setForm((prev) => ({
      ...prev,
      [campo]: value,
    }));
  };

  const handleSalvar = async () => {
    await onboardingCorrigirService.corrigirDocumentos(form);
    setEditando(false);
    onSalvo("DOCUMENTOS");
  };

  return (
    <div className="card">
      <h3>Documentos</h3>

      {podeEditar && observacao && (
        <div style={{ background: "#fdecea", padding: 12, marginBottom: 16 }}>
          <strong>Observação do RH:</strong>
          <p>{observacao}</p>
        </div>
      )}


      <TextField
        label="CPF"
        value={maskCPF(form.cpf)}
        onChange={e =>
          setForm({
            ...form,
            cpf: onlyNumbers(e.target.value),
          })
        }
        disabled={!editando}
        fullWidth
        autoFocus
        margin="dense"
        inputProps={{ maxLength: 14 }}
      />

      <TextField
        label="PIS / PASEP"
        value={maskPisPasep(form.pisPasep)}
        onChange={handleChange("pisPasep")}
        disabled={!editando}
        fullWidth
        margin="dense"
        inputProps={{ maxLength: 14 }}
      />

      <FormControlLabel
        control={
          <Checkbox
            checked={form.possuiCtpsFisica}
            onChange={handleChange("possuiCtpsFisica")}
            disabled={!editando}
          />
        }
        label="Possui CTPS física"
      />

      {form.possuiCtpsFisica && (
        <>
          <TextField
            label="Número da CTPS"
            value={form.ctpsNumero || ""}
            onChange={handleChange("ctpsNumero")}
            disabled={!editando}
            fullWidth
            margin="dense"
          />

          <TextField
            label="Série da CTPS"
            value={form.ctpsSerie || ""}
            onChange={handleChange("ctpsSerie")}
            disabled={!editando}
            fullWidth
            margin="dense"
          />
        </>
      )}

      <TextField
        label="Registro estrangeiro"
        value={form.registroEstrangeiro || ""}
        onChange={handleChange("registroEstrangeiro")}
        disabled={!editando}
        fullWidth
        margin="dense"
      />

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
