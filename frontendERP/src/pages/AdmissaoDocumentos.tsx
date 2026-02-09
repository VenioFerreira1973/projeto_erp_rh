import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {
    TextField,
    Button,
    CircularProgress,
    FormControlLabel,
    Checkbox,
} from "@mui/material";
import AdmissaoStepper from "../components/admissao/AdmissaoStepper";
import { admissaoService } from "../services/admissaoService";
import { useAdmissao } from "../context/AdmissaoContext";

export default function AdmissaoDocumentos() {
    const { status, loading, refreshStatus } = useAdmissao();
    const [documento, setDocumentos] = useState<any>({
        cpf: "",
        pisPasep: "",
        possuiCtpsFisica: false,
        numeroCarteiraTrabalho: "",
        serieCarteiraTrabalho: "",
        registroEstrangeiro: "",

    });
    const [saving, setSaving] = useState(false);

    const maskCPF = (value: string) => {
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
        if (!status) return;

        async function fetchDados() {
            const dadosResponse = await admissaoService.obterDocumentos();
            setDocumentos({
                cpf: dadosResponse.cpf || "",
                pisPasep: dadosResponse.pisPasep || "",
                possuiCtpsFisica: !!dadosResponse.possuiCtpsFisica,
                numeroCarteiraTrabalho: dadosResponse.numeroCarteiraTrabalho || "",
                serieCarteiraTrabalho: dadosResponse.serieCarteiraTrabalho || "",
                registroEstrangeiro: dadosResponse.registroEstrangeiro || "",

            });
        }

        fetchDados();
    }, [status]);


    const handleChange = (e: any) => {
        const { name, value } = e.target;

        if (name === "cpf" || name === "pisPasep") {
            setDocumentos((prev:any) => ({
                ...prev,
                [name]: value.replace(/\D/g, ""),
            }));
            return;
        }
        setDocumentos((prev:any) => ({ ...prev, [name]: value }));
    };

    const handleSalvar = async () => {

        if (
            documento.possuiCtpsFisica &&
            (!documento.numeroCarteiraTrabalho || !documento.serieCarteiraTrabalho)
        ) {
            toast.error("Informe número e série da CTPS física", { autoClose: 3000 });
            return;
        }

        setSaving(true);

        try {
            await admissaoService.salvarDocumentos(documento);
            await refreshStatus();
        } catch (_err) {
            void _err;
            toast.error("Erro ao salvar documentos", { autoClose: 3000, });
        } finally {
            setSaving(false);
        }
    };


    if (loading) return <CircularProgress />;

    return (
        <div className="admissao-container">
            <AdmissaoStepper />
            <form className="admissao-form">
                <TextField
                    label="CPF"
                    name="cpf"
                    value={maskCPF(documento.cpf)}
                    onChange={handleChange}
                    fullWidth
                    sx={{ mb: 1 }}
                    inputProps={{ maxLength: 14 }}
                    autoFocus
                />
                <TextField
                    label="PIS / PASEP"
                    name="pisPasep"
                    value={maskPisPasep(documento.pisPasep)}
                    onChange={handleChange}
                    fullWidth
                    sx={{ mb: 1 }}
                    inputProps={{ maxLength: 14 }}
                />
                <TextField
                    label="Registro Estrangeiro"
                    name="registroEstrangeiro"
                    value={documento.registroEstrangeiro}
                    onChange={handleChange}
                    fullWidth
                    sx={{ mb: 1 }}
                />
                <FormControlLabel
                    control={
                        <Checkbox
                            checked={documento.possuiCtpsFisica}
                            onChange={(e:any) => {
                                const checked = e.target.checked;

                                setDocumentos((prev:any) => ({
                                    ...prev,
                                    possuiCtpsFisica: checked,
                                    numeroCarteiraTrabalho: checked ? prev.numeroCarteiraTrabalho : "",
                                    serieCarteiraTrabalho: checked ? prev.serieCarteiraTrabalho : "",
                                }));
                            }}
                        />
                    }
                    label="Você possui Carteira de Trabalho Física?"
                />
                {documento.possuiCtpsFisica && (
                    <>
                        <TextField
                            label="Número Carteira de Trabalho"
                            name="numeroCarteiraTrabalho"
                            value={documento.numeroCarteiraTrabalho}
                            onChange={handleChange}
                            fullWidth
                            sx={{ mb: 1 }}
                        />
                        <TextField
                            label="Série Carteira de Trabalho"
                            name="serieCarteiraTrabalho"
                            value={documento.serieCarteiraTrabalho}
                            onChange={handleChange}
                            fullWidth
                            sx={{ mb: 1 }}
                        />
                    </>
                )}
                <Button
                    variant="contained"
                    color="primary"
                    onClick={handleSalvar}
                    disabled={saving}
                    style={{ marginTop: 20 }}
                >
                    {saving ? "Salvando..." : "Continuar"}
                </Button>
            </form>
        </div>
    );
}
