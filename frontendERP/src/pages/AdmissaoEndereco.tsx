import React, { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { TextField, Button, MenuItem, CircularProgress } from "@mui/material";
import { admissaoService } from "../services/admissaoService";
import { useAdmissao } from "../context/AdmissaoContext";
import AdmissaoStepper from "../components/admissao/AdmissaoStepper";
import { buscarEnderecoPorCep } from "../services/viacepService";

export default function AdmissaoEndereco(): JSX.Element {
  const { status, loading, refreshStatus } = useAdmissao() as any;
  const [endereco, setEndereco] = useState<any>({ tipoEndereco: "", cep: "", logradouro: "", numero: "", complemento: "", bairro: "", municipio: "", uf: "", pais: "", dataCriacao: "" });
  const [saving, setSaving] = useState(false);

  const tipoEnderecoOptions = [
    { value: "RESIDENCIAL", label: "Residencial" },
    { value: "COMERCIAL", label: "Comercial" },
    { value: "CORRESPONDENCIA", label: "Correspondência" },
  ];

  useEffect(() => {
    if (!status) return;
    async function fetchEndereco() {
      const enderecoResponse = await admissaoService.obterEndereco();
      setEndereco({ tipoEndereco: enderecoResponse.tipoEndereco || "", cep: enderecoResponse.cep || "", logradouro: enderecoResponse.logradouro || "", numero: enderecoResponse.numero || "", complemento: enderecoResponse.complemento || "", bairro: enderecoResponse.bairro || "", municipio: enderecoResponse.municipio || "", uf: enderecoResponse.uf || "", pais: enderecoResponse.pais || "", dataCriacao: enderecoResponse.dataCriacao || "" });
    }
    fetchEndereco();
  }, [status]);

  const handleCepBlur = async () => {
    try {
      const dados = await buscarEnderecoPorCep(endereco.cep);
      if (dados.erro) { toast.error("CEP não encontrado", { autoClose: 3000 }); return; }
      setEndereco((prev: any) => ({ ...prev, logradouro: dados.logradouro || "", bairro: dados.bairro || "", municipio: dados.localidade || "", uf: dados.uf || "", codigoIbgeMunicipio: dados.ibge || "" }));
    } catch { toast.error("Erro ao buscar endereço", { autoClose: 3000 }); }
  };

  const handleChange = (e: any) => { const { name, value } = e.target; setEndereco((prev: any) => ({ ...prev, [name]: value })); };

  const handleSalvar = async () => {
    setSaving(true);
    try { await admissaoService.salvarEndereco(endereco); await refreshStatus(); } catch (err: any) { const mensagem = err.response?.data?.message || "Erro ao finalizar processo"; toast.error(mensagem, { autoClose: 3000 }); } finally { setSaving(false); }
  };

  if (loading) return <CircularProgress />;

  return (
    <div className="admissao-container">
      <AdmissaoStepper />
      <form className="admissao-form">
        <TextField label="CEP" name="cep" value={endereco.cep} onChange={handleChange} onBlur={handleCepBlur} fullWidth autoFocus />
        <TextField label="Logradouro" name="logradouro" value={endereco.logradouro} onChange={handleChange} fullWidth />
        <TextField label="Número" name="numero" value={endereco.numero} onChange={handleChange} fullWidth />
        <TextField label="Complemento" name="complemento" value={endereco.complemento} onChange={handleChange} fullWidth />
        <TextField label="Bairro" name="bairro" value={endereco.bairro} onChange={handleChange} fullWidth />
        <TextField label="Município" name="municipio" value={endereco.municipio} onChange={handleChange} fullWidth />
        <TextField label="UF" name="uf" value={endereco.uf} onChange={handleChange} fullWidth />
        <TextField label="País" name="pais" value={endereco.pais} onChange={handleChange} fullWidth />
        <TextField select label="Tipo Endereço" name="tipoEndereco" value={endereco.tipoEndereco} onChange={handleChange} fullWidth>
          {tipoEnderecoOptions.map((option) => (<MenuItem key={option.value} value={option.value}>{option.label}</MenuItem>))}
        </TextField>

        <Button variant="contained" color="primary" onClick={handleSalvar} disabled={saving} style={{ marginTop: 20 }}>
          {saving ? "Salvando..." : "Continuar "}
        </Button>
      </form>
    </div>
  );
}
