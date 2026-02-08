import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {
    TextField,
    Button,
    MenuItem,
    CircularProgress,
} from "@mui/material";
import { onboardingService } from "../services/onboardingService";
import { useOnboarding } from "../context/OnboardingContext";
import OnboardingStepper from "../components/onboarding/OnboardingStepper";
import { buscarEnderecoPorCep } from "../services/viacepService";

export default function OnboardingEndereco() {
    const { status, loading, refreshStatus } = useOnboarding();
    const [endereco, setEndereco] = useState({
        tipoEndereco: "",
        cep: "",
        logradouro: "",
        numero: "",
        complemento: "",
        bairro: "",
        municipio: "",
        uf: "",
        pais: "",
        dataCriacao: "",
    });
    const [saving, setSaving] = useState(false);

    const tipoEnderecoOptions = [
        { value: "RESIDENCIAL", label: "Residencial" },
        { value: "COMERCIAL", label: "Comercial" },
        { value: "CORRESPONDENCIA", label: "Correspondência" },
    ];

    useEffect(() => {
        if (!status) return;

        async function fetchEndereco() {
            const enderecoResponse = await onboardingService.getEndereco();
            setEndereco({
                tipoEndereco: enderecoResponse.tipoEndereco || "",
                cep: enderecoResponse.cep || "",
                logradouro: enderecoResponse.logradouro || "",
                numero: enderecoResponse.numero || "",
                complemento: enderecoResponse.complemento || "",
                bairro: enderecoResponse.bairro || "",
                municipio: enderecoResponse.municipio || "",
                uf: enderecoResponse.uf || "",
                pais: enderecoResponse.pais || "",
                dataCriacao: enderecoResponse.dataCriacao || ""
            });
        }

        fetchEndereco();
    }, [status]);


    const handleCepBlur = async () => {
        try {
            const dados = await buscarEnderecoPorCep(endereco.cep);

            if (dados.erro) {
                toast.error("CEP não encontrado", { autoClose: 3000, });
                return;
            }

            setEndereco((prev) => ({
                ...prev,
                logradouro: dados.logradouro || "",
                bairro: dados.bairro || "",
                municipio: dados.localidade || "",
                uf: dados.uf || "",
                codigoIbgeMunicipio: dados.ibge || "",
            }));
        } catch {
            toast.error("Erro ao buscar endereço", { autoClose: 3000, });
        }
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setEndereco((prev) => ({ ...prev, [name]: value }));
    };

    const handleSalvar = async () => {
        setSaving(true);
        try {
            await onboardingService.salvarEndereco(endereco);
            await refreshStatus();

        } catch (err) {
            console.error("Erro completo:", err);
            const mensagem = err.response?.data?.message || "Erro ao finalizar processo";
            toast.error(mensagem, { autoClose: 3000, });
        } finally {
            setSaving(false);
        }
    };

    if (loading) return <CircularProgress />;

    return (
        <div className="onboarding-container">
            <OnboardingStepper />

            <form className="onboarding-form">
                <TextField
                    label="CEP"
                    name="cep"
                    value={endereco.cep}
                    onChange={handleChange}
                    onBlur={handleCepBlur}
                    fullWidth
                    autoFocus
                />
                <TextField
                    label="Logradouro"
                    name="logradouro"
                    value={endereco.logradouro}
                    onChange={handleChange}
                    fullWidth
                />
                <TextField
                    label="Número"
                    name="numero"
                    value={endereco.numero}
                    onChange={handleChange}
                    fullWidth
                />
                <TextField
                    label="Complemento"
                    name="complemento"
                    value={endereco.complemento}
                    onChange={handleChange}
                    fullWidth
                />
                <TextField
                    label="Bairro"
                    name="bairro"
                    value={endereco.bairro}
                    onChange={handleChange}
                    fullWidth
                />
                <TextField
                    label="Município"
                    name="municipio"
                    value={endereco.municipio}
                    onChange={handleChange}
                    fullWidth
                />
                <TextField
                    label="UF"
                    name="uf"
                    value={endereco.uf}
                    onChange={handleChange}
                    fullWidth
                />
                <TextField
                    label="País"
                    name="pais"
                    value={endereco.pais}
                    onChange={handleChange}
                    fullWidth
                />
                <TextField
                    select
                    label="Tipo Endereço"
                    name="tipoEndereco"
                    value={endereco.tipoEndereco}
                    onChange={handleChange}
                    fullWidth
                >
                    {tipoEnderecoOptions.map((option) => (
                        <MenuItem key={option.value} value={option.value}>
                            {option.label}
                        </MenuItem>
                    ))}
                </TextField>

                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleSalvar}
                    disabled={saving}
                    style={{ marginTop: 20 }}
                >
                    {saving ? "Salvando..." : "Continuar "}
                </Button>
            </form>
        </div>
    );
}
