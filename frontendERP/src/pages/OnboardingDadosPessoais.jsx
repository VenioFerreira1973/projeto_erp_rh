import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {
    TextField,
    Button,
    CircularProgress,
    MenuItem,
    Divider,
    Paper,
} from "@mui/material";
import OnboardingStepper from "../components/onboarding/OnboardingStepper";
import { onboardingService } from "../services/onboardingService";
import { useOnboarding } from "../context/OnboardingContext";
import DependentesForm from "../components/onboarding/DependentesForm";

export default function OnboardingDadosPessoais() {
    const { status, loading, refreshStatus } = useOnboarding();
    const [dados, setDados] = useState({
        dataNascimento: "",
        estadoCivil: "",
        genero: "",
        corRaca: "",
        nacionalidade: "",
    });
    const [saving, setSaving] = useState(false);
    const [dependentes, setDependentes] = useState([]);

    const estadoCivilOptions = [
        { value: "SOLTEIRO", label: "Solteiro" },
        { value: "CASADO", label: "Casado" },
        { value: "DIVORCIADO", label: "Divorciado" },
        { value: "VIUVO", label: "Viúvo" },
    ];

    const generoOptions = [
        { value: "MASCULINO", label: "Masculino" },
        { value: "FEMININO", label: "Feminino" },
        { value: "OUTRO", label: "Outros" },
    ];

    const corRacaOptions = [
        { value: "BRANCO", label: "Branco" },
        { value: "NEGRO", label: "Negro" },
        { value: "PARDO", label: "Pardo" },
        { value: "INDIGENA", label: "Indígena" },
        { value: "AMARELO", label: "Amarelo" },
    ];

    useEffect(() => {
        if (!status) return;

        async function fetchDados() {
            const dadosResponse = await onboardingService.getDadosPessoais();
            setDados({
                dataNascimento: dadosResponse.dataNascimento || "",
                estadoCivil: dadosResponse.estadoCivil || "",
                genero: dadosResponse.genero || "",
                corRaca: dadosResponse.corRaca || "",
                nacionalidade: dadosResponse.nacionalidade || "",
            });
        }

        fetchDados();
    }, [status]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setDados((prev) => ({ ...prev, [name]: value }));
    };

    const handleSalvar = async () => {
        setSaving(true);
        try {
            await onboardingService.salvarDadosPessoais(dados);
            if (dependentes.length > 0) {
                const payload = dependentes.map(({ tempId, ...rest }) => rest);
                await onboardingService.salvarDependentes(payload);
            }
            await refreshStatus();
        } catch (err) {
            toast.error("Erro ao salvar dados pessoais", { autoClose: 3000, });
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
                    label="Data de Nascimento"
                    type="date"
                    name="dataNascimento"
                    value={dados.dataNascimento}
                    onChange={handleChange}
                    fullWidth
                    sx={{ mb: 1 }}
                    InputLabelProps={{ shrink: true }}
                    autoFocus
                />
                <TextField
                    select
                    label="Estado Civil"
                    name="estadoCivil"
                    value={dados.estadoCivil}
                    onChange={handleChange}
                    fullWidth
                    sx={{ mb: 1 }}
                >
                    {estadoCivilOptions.map((option) => (
                        <MenuItem key={option.value} value={option.value}>
                            {option.label}
                        </MenuItem>
                    ))}
                </TextField>
                <TextField
                    select
                    label="Gênero"
                    name="genero"
                    value={dados.genero}
                    onChange={handleChange}
                    fullWidth
                    sx={{ mb: 1 }}
                >
                    {generoOptions.map((option) => (
                        <MenuItem key={option.value} value={option.value}>
                            {option.label}
                        </MenuItem>
                    ))}
                </TextField>
                <TextField
                    select
                    label="Cor / Raça"
                    name="corRaca"
                    value={dados.corRaca}
                    onChange={handleChange}
                    fullWidth
                    sx={{ mb: 1 }}
                >
                    {corRacaOptions.map((option) => (
                        <MenuItem key={option.value} value={option.value}>
                            {option.label}
                        </MenuItem>
                    ))}
                </TextField>
                <TextField
                    label="Nacionalidade"
                    name="nacionalidade"
                    value={dados.nacionalidade}
                    onChange={handleChange}
                    fullWidth
                    sx={{ mb: 1 }}
                />
                <DependentesForm
                    dependentes={dependentes}
                    setDependentes={setDependentes}
                />
                <Divider sx={{ my: 3 }} />

                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleSalvar}
                    disabled={saving}
                    fullWidth
                >
                    {saving ? "Salvando..." : "Continuar"}
                </Button>
            </form>
        </div>

    );
}
