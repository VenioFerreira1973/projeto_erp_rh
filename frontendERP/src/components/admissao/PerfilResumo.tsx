import React, { useEffect, useState } from "react";
import { Avatar, TextField, Button } from "@mui/material";
import { admissaoCorrigirService } from "../../services/admissaoCorrigirService";
import { formatarDataBR } from "../../utils/dateUtils";

export default function PerfilResumo({ dadosPessoais, statusValidacao, observacao, fotoUrl, onSalvo, permitirCorrecao }: any): JSX.Element {
  const podeEditar = permitirCorrecao && statusValidacao === "REPROVADO";
  const [editando, setEditando] = useState(false);
  const [form, setForm] = useState<any>(dadosPessoais);

  useEffect(() => {
    setForm(dadosPessoais);
  }, [dadosPessoais]);

  if (!form) return <p>Carregando...</p>;

  const handleChange = (campo: string) => (event: any) => setForm((prev: any) => ({ ...prev, [campo]: event.target.value }));

  const handleSalvar = async () => {
    await admissaoCorrigirService.corrigirDadosPessoais(form);
    setEditando(false);
    onSalvo("DADOS_PESSOAIS");
  };

  return (
    <div className="card">
      <h3>Dados Pessoais</h3>

      <Avatar src={fotoUrl} sx={{ width: 100, height: 100, mb: 2 }}>{!fotoUrl && dadosPessoais.nomeCompleto?.charAt(0)}</Avatar>

      {podeEditar && observacao && (
        <div style={{ background: "#fdecea", padding: 12, marginBottom: 16 }}>
          <strong>Observação do RH:</strong>
          <p>{observacao}</p>
        </div>
      )}

      {editando ? (
        <TextField label="Data de nascimento" type="date" value={form.dataNascimento ?? ""} onChange={handleChange("dataNascimento")} fullWidth margin="dense" InputLabelProps={{ shrink: true }} />
      ) : (
        <TextField label="Data de nascimento" value={formatarDataBR(form.dataNascimento)} disabled fullWidth autoFocus margin="dense" />
      )}

      <TextField label="Estado civil" value={form.estadoCivil} onChange={handleChange("estadoCivil")} disabled={!editando} fullWidth margin="dense" />
      <TextField label="Gênero" value={form.genero} onChange={handleChange("genero")} disabled={!editando} fullWidth margin="dense" />
      <TextField label="Raça/Cor" value={form.corRaca} onChange={handleChange("corRaca")} disabled={!editando} fullWidth margin="dense" />
      <TextField label="Nacionalidade" value={form.nacionalidade} onChange={handleChange("nacionalidade")} disabled={!editando} fullWidth margin="dense" />

      {podeEditar && (
        <Button sx={{ mt: 2 }} variant={editando ? "contained" : "outlined"} color="warning" onClick={editando ? handleSalvar : () => setEditando(true)}>
          {editando ? "Salvar correção" : "Corrigir dados"}
        </Button>
      )}
    </div>
  );
}
