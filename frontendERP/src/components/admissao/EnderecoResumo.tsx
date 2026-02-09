import React, { useEffect, useState } from "react";
import { TextField, Button, MenuItem } from "@mui/material";
import { buscarEnderecoPorCep } from "../../services/viacepService";
import { admissaoCorrigirService } from "../../services/admissaoCorrigirService";

export default function EnderecoResumo({ endereco, statusValidacao, observacao, onSalvo, permitirCorrecao }: any): JSX.Element {
  const podeEditar = permitirCorrecao && statusValidacao === "REPROVADO";
  const [editando, setEditando] = useState(false);
  const [form, setForm] = useState<any>(endereco);

  const tipoEnderecoOptions = [
    { value: "RESIDENCIAL", label: "Residencial" },
    { value: "COMERCIAL", label: "Comercial" },
    { value: "CORRESPONDENCIA", label: "Correspondência" },
  ];

  useEffect(() => {
    setForm(endereco);
  }, [endereco]);

  if (!form) return <p>Nenhum endereço encontrado.</p>;

  const handleChange = (e: any) => {
    const { name, value } = e.target;
    setForm((prev: any) => ({ ...prev, [name]: value }));
  };

  const handleCepBlur = async () => {
    if (!editando || !form.cep) return;
    try {
      const dados = await buscarEnderecoPorCep(form.cep);
      if (dados.erro) return;
      setForm((prev: any) => ({ ...prev, logradouro: dados.logradouro || "", bairro: dados.bairro || "", municipio: dados.localidade || "", uf: dados.uf || "" }));
    } catch {
      // silencioso no resumo
    }
  };

  const handleSalvar = async () => {
    await admissaoCorrigirService.corrigirEndereco(form);
    setEditando(false);
    onSalvo("ENDERECO");
  };

  return (
    <div className="card">
      <h3>Endereço</h3>

      {podeEditar && observacao && (
        <div style={{ background: "#fdecea", padding: 12, marginBottom: 16 }}>
          <strong>Observação do RH:</strong>
          <p>{observacao}</p>
        </div>
      )}

      <TextField label="CEP" name="cep" value={form.cep} onChange={handleChange} onBlur={handleCepBlur} disabled={!editando} fullWidth autoFocus margin="dense" />
      <TextField label="Logradouro" name="logradouro" value={form.logradouro} onChange={handleChange} disabled={!editando} fullWidth margin="dense" />
      <TextField label="Número" name="numero" value={form.numero} onChange={handleChange} disabled={!editando} fullWidth margin="dense" />
      <TextField label="Complemento" name="complemento" value={form.complemento} onChange={handleChange} disabled={!editando} fullWidth margin="dense" />
      <TextField label="Bairro" name="bairro" value={form.bairro} onChange={handleChange} disabled={!editando} fullWidth margin="dense" />
      <TextField label="Município" name="municipio" value={form.municipio} onChange={handleChange} disabled={!editando} fullWidth margin="dense" />
      <TextField label="UF" name="uf" value={form.uf} onChange={handleChange} disabled={!editando} fullWidth margin="dense" />
      <TextField label="País" name="pais" value={form.pais} onChange={handleChange} disabled={!editando} fullWidth margin="dense" />

      <TextField select label="Tipo de endereço" name="tipoEndereco" value={form.tipoEndereco} onChange={handleChange} disabled={!editando} fullWidth margin="dense">
        {tipoEnderecoOptions.map((option) => (
          <MenuItem key={option.value} value={option.value}>{option.label}</MenuItem>
        ))}
      </TextField>

      {podeEditar && (
        <Button sx={{ mt: 2 }} variant={editando ? "contained" : "outlined"} color="warning" onClick={editando ? handleSalvar : () => setEditando(true)}>
          {editando ? "Salvar correção" : "Corrigir endereço"}
        </Button>
      )}
    </div>
  );
}
