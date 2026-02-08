import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {
    TextField,
    Button,
    CircularProgress,
    IconButton,
    MenuItem,
    Paper,
    Box,
    Stack,
    Tooltip,
} from "@mui/material";
import DeleteOutlineIcon from "@mui/icons-material/DeleteOutline";

import OnboardingStepper from "../components/onboarding/OnboardingStepper";
import { onboardingService } from "../services/onboardingService";
import { useOnboarding } from "../context/OnboardingContext";


export default function OnboardingDocumentosAnexos() {
    const { status, loading, refreshStatus } = useOnboarding();
    const navigate = useNavigate();

    const [documento, setDocumento] = useState({
        tipo: "",
        observacao: "",
        dataValidade: "",
        arquivo: null,
    });

    const [documentos, setDocumentos] = useState([]);
    const [saving, setSaving] = useState(false);;

    const tipoDocumentoOptions = [
        { value: "RG", label: "RG" },
        { value: "CNH", label: "CNH" },
        { value: "CTPS", label: "CTPS" },
        { value: "PASSAPORTE", label: "Passaport" },
        { value: "COMPROVANTE_RESIDENCIA", label: "Comprovante de Residência" },
        { value: "CERTIFICADO", label: "Certificado" },
        { value: "FOTO_PERFIL", label: "Foto Perfil" },
        { value: "OUTRO", label: "Outro" },
    ];

    useEffect(() => {
        if (!status) return;

        async function fetchDados() {
            try {
                const dadosResponse = await onboardingService.getDocumentosAnexos();

                const documentos = Array.isArray(dadosResponse?.documentos)
                    ? dadosResponse.documentos
                    : [];

                setDocumentos(
                    documentos.map((d) => ({
                        id: d.id,
                        tipo: d.tipoDocumentoAnexo || "",
                        observacao: dadosResponse?.observacao || "",
                        dataValidade: d.dataValidade || "",
                        arquivo: null,
                        arquivoUrl: d.arquivoUrl,
                    }))
                );
            } catch (error) {
                console.error("Erro ao buscar documentos anexos:", error);
                setDocumentos([]);
            }
        }

        fetchDados();
    }, [status]);


    const handleArquivoChange = (e) => {
        const file = e.target.files[0];
        if (!file) return;

        setDocumento((prev) => ({
            ...prev,
            arquivo: file,
        }));
    };

    const handleRemoverDocumento = (index) => {
        setDocumentos((prev) => prev.filter((_, i) => i !== index));
    };

    const handleAdicionarDocumento = () => {
        if (!documento.tipo || !documento.arquivo) {
            toast.error("Informe o tipo e selecione um arquivo", { autoClose: 3000, });
            return;
        }

        setDocumentos((prev) => [...prev, documento]);

        setDocumento({
            tipo: "",
            observacao: "",
            dataValidade: "",
            arquivo: null,
        });
    };


    const handleChange = (e) => {
        const { name, value } = e.target;
        setDocumento((prev) => ({
            ...prev,
            [name]: value,
        }));
    };

    const handleSalvar = async () => {
        if (documentos.length === 0) {
            toast.error("Adicione pelo menos um documento", { autoClose: 3000, });
            return;
        }

        setSaving(true);

        try {
            const documentosParaUpload = documentos.filter(d => d.arquivo);
            const arquivos = documentosParaUpload.map(d => d.arquivo);

            const urls = await onboardingService.uploadDocumentosAnexos(arquivos);

            let urlIndex = 0;

            const payload = documentos.map((doc) => ({
                id: doc.id,
                tipoDocumentoAnexo: doc.tipo,
                observacao: doc.observacao,
                dataValidade: doc.dataValidade,
                arquivoUrl: doc.arquivo
                    ? urls[urlIndex++]
                    : doc.arquivoUrl
            }));

            await onboardingService.salvarDocumentosAnexos(payload);
            await onboardingService.enviarParaAnalise();
            refreshStatus();

            toast.success(
                "Documentos enviados! Seu onboarding está em análise pelo RH.",
                { autoClose: 4000 }
            );

            navigate("/home", { replace: true });
        } catch (err) {
            console.error(err);
            toast.error("Erro ao salvar documentos", { autoClose: 3000 });
        } finally {
            setSaving(false);
        }
    };



    if (loading) return <CircularProgress />;

    return (
        <div className="onboarding-container">
            <OnboardingStepper />

            <Paper sx={{ p: 2, mb: 2 }}>
                <Box display="flex" flexDirection="column" gap={2}>
                    <TextField
                        select
                        label="Tipo Documento"
                        name="tipo"
                        value={documento.tipo}
                        onChange={handleChange}
                        fullWidth
                        autoFocus                    >
                        {tipoDocumentoOptions.map((option) => (
                            <MenuItem key={option.value} value={option.value}>
                                {option.label}
                            </MenuItem>
                        ))}
                    </TextField>
                    <TextField
                        label="Data de Validade"
                        type="date"
                        name="dataValidade"
                        value={documento.dataValidade}
                        onChange={handleChange}
                        fullWidth
                        InputLabelProps={{ shrink: true }}
                    />

                    <Button
                        size="small"
                        variant="outlined"
                        component="label"
                        sx={{ alignSelf: "flex-start" }}
                    >
                        Selecionar arquivo
                        <input type="file" hidden onChange={handleArquivoChange} />
                    </Button>

                    {documento.arquivo && (
                        <Box sx={{ fontSize: 13, color: "text.secondary" }}>
                            {documento.arquivo.name}
                        </Box>
                    )}

                    {documento.arquivo && <div>Arquivo: {documento.arquivo.name}</div>}

                    <Box display="flex" justifyContent="flex-end">
                        <Button
                            size="small"
                            variant="outlined"
                            onClick={handleAdicionarDocumento}
                            disabled={!documento.tipo || !documento.arquivo}
                            sx={{ textTransform: "none", fontSize: 13 }}
                        >
                            Adicionar documento
                        </Button>
                    </Box>
                </Box>
            </Paper>

            {documentos.map((doc, index) => (
                <Paper
                    key={index}
                    variant="outlined"
                    sx={{ p: 1, mb: 0.5, backgroundColor: "grey.50" }}
                >
                    <Stack direction="row" justifyContent="space-between" alignItems="center">
                        <Box>
                            <strong>{doc.tipo}</strong>
                            <Box sx={{ fontSize: 13, color: "text.secondary" }}>
                                {doc.arquivo?.name || "Arquivo já enviado"}
                            </Box>
                        </Box>

                        <Tooltip title="Remover" arrow>
                            <IconButton
                                size="small"
                                onClick={() => handleRemoverDocumento(index)}
                                sx={{
                                    color: "text.secondary",
                                    "&:hover": {
                                        color: "error.main",
                                    },
                                }}
                            >
                                <DeleteOutlineIcon fontSize="small" />
                            </IconButton>
                        </Tooltip>
                    </Stack>
                </Paper>
            ))}

            <Button
                variant="contained"
                color="primary"
                onClick={handleSalvar}
                disabled={saving}
                sx={{ mt: 2 }}
            >
                {saving ? "Enviando..." : "Enviar para análise"}
            </Button>
        </div>
    );
}
