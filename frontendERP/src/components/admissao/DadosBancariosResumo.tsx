import React, { useEffect, useState } from "react";
import { TextField, Button, MenuItem } from "@mui/material";
import { admissaoCorrigirService } from "../../services/admissaoCorrigirService";
import { buscarBancoPeloCodigo } from "../../services/viacodBancoService";

export default function DadosBancariosResumo({ dadosBancarios, statusValidacao, observacao, onSalvo, permitirCorrecao }: any): JSX.Element {
  const podeEditar = permitirCorrecao && statusValidacao === "REPROVADO";
  const [editando, setEditando] = useState(false);
  const [form, setForm] = useState<any>(dadosBancarios);

  const tipoContaOptions = [
    { value: "SALARIO", label: "Salário" },
    { value: "CORRENTE", label: "Corrente" },
    { value: "PAGAMENTO", label: "Pagamento" },
    { value: "POUPANCA", label: "Poupança" },
  ];

  const formaPagamentoOptions = [
    { value: "TED", label: "TED" },
    { value: "PIX", label: "PIX" },
    { value: "CHEQUE", label: "Cheque" },
  ];

  useEffect(() => {
    setForm(dadosBancarios);
  }, [dadosBancarios]);

  if (!form) return <p>Nenhum dado bancário encontrado.</p>;

  const handleChange = (campo: string) => (event: any) => {
    setForm((prev: any) => ({ ...prev, [campo]: event.target.value }));
  };

  const handleSalvar = async () => {
    await admissaoCorrigirService.corrigirDadosBancarios(form);
    setEditando(false);
    onSalvo("DADOS_BANCARIOS");
  };

  const handleBancoBlur = async () => {
    try {
      const dados = await buscarBancoPeloCodigo(form.bancoCodigo as any);
      if (!dados) {
        // toast.error handled upstream
        return;
      }

      setForm((prev: any) => ({ ...prev, bancoNome: dados.name || "" }));
    } catch {
      // ignore
    }
  };

  return (
    <div className="card">
      <h3>Dados Bancários</h3>

      {podeEditar && observacao && (
        <div style={{ background: "#fdecea", padding: 12, marginBottom: 16 }}>
          <strong>Observação do RH:</strong>
          <p>{observacao}</p>
        </div>
      )}

      <TextField label="Código do banco" value={form.bancoCodigo} onChange={handleChange("bancoCodigo")} onBlur={handleBancoBlur} disabled={!editando} fullWidth autoFocus margin="dense" />

      <TextField label="Nome do banco" value={form.bancoNome} onChange={handleChange("bancoNome")} disabled={!editando} fullWidth margin="dense" />

      <TextField label="Agência" value={form.agencia} onChange={handleChange("agencia")} disabled={!editando} fullWidth margin="dense" />

      <TextField label="Conta" value={form.conta} onChange={handleChange("conta")} disabled={!editando} fullWidth margin="dense" />

      <TextField label="Dígito da conta" value={form.digitoConta} onChange={handleChange("digitoConta")} disabled={!editando} fullWidth margin="dense" />

      <TextField select label="Tipo da Conta" name="tipoConta" value={form.tipoConta} onChange={handleChange("tipoConta")} disabled={!editando} fullWidth margin="dense">
        {tipoContaOptions.map((option) => (
          <MenuItem key={option.value} value={option.value}>{option.label}</MenuItem>
        ))}
      </TextField>

      <TextField select label="Forma Pagamento" name="formaPagamento" value={form.formaPagamento} onChange={handleChange("formaPagamento")} disabled={!editando} fullWidth margin="dense">
        {formaPagamentoOptions.map((option) => (
          <MenuItem key={option.value} value={option.value}>{option.label}</MenuItem>
        ))}
      </TextField>

      <TextField label="Chave Pix" value={form.chavePix || ""} onChange={handleChange("chavePix")} disabled={!editando} fullWidth margin="dense" />

      {podeEditar && (
        <Button sx={{ mt: 2 }} variant={editando ? "contained" : "outlined"} color="warning" onClick={editando ? handleSalvar : () => setEditando(true)}>
          {editando ? "Salvar correção" : "Corrigir dados"}
        </Button>
      )}
    </div>
  );
}
