import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {
    TextField,
    Button,
    CircularProgress,
    MenuItem,
} from "@mui/material";
import AdmissaoStepper from "../components/admissao/AdmissaoStepper";
import { admissaoService } from "../services/admissaoService";
import { useAdmissao } from "../context/AdmissaoContext";
import { buscarBancoPeloCodigo } from "../services/viacodBancoService";

export default function AdmissaoDadosBancarios() {
    const { status, loading, refreshStatus } = useAdmissao();
    const [banco, setBanco] = useState<any>({
        bancoCodigo: "",
        bancoNome: "",
        agencia: "",
        conta: "",
        digitoConta: "",
        tipoConta: "",
        chavePix: "",
        formaPagamento: "",

    });
    const [saving, setSaving] = useState(false);

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
        if (!status) return;

        async function fetchDados() {
            const dadosResponse = await admissaoService.obterDadosBancarios();
            setBanco({
                bancoCodigo: dadosResponse.bancoCodigo || "",
                bancoNome: dadosResponse.bancoNome || "",
                agencia: dadosResponse.agencia || "",
                conta: dadosResponse.conta || "",
                digitoConta: dadosResponse.digitoConta || "",
                tipoConta: dadosResponse.tipoConta || "",
                chavePix: dadosResponse.chavePix || "",
                formaPagamento: dadosResponse.formaPagamento || "",
            });
        }

        fetchDados();
    }, [status]);


    const handleBancoBlur = async () => {
        try {
            const dados = await buscarBancoPeloCodigo(banco.bancoCodigo);

            if (!dados) {
                toast.error("Código de banco não encontrado", { autoClose: 3000 });
                return;
            }

            setBanco((prev:any) => ({
                ...prev,
                bancoNome: dados.name || "",
            }));
        } catch (err) {
            void err;
            toast.error("Erro ao buscar o banco", { autoClose: 3000, });
        }
    };


    const handleChange = (e: any) => {
        const { name, value } = e.target;
        setBanco((prev:any) => ({ ...prev, [name]: value }));
    };

    const handleSalvar = async () => {
        setSaving(true);
        try {
            await admissaoService.salvarDadosBancarios(banco);
            await refreshStatus();
        } catch (err) {
            void err;
            toast.error("Erro ao salvar dados bancários", { autoClose: 3000, });
        } finally {
            setSaving(false);
        }
    };


    if (loading) return <CircularProgress />;

    return (
        <div className="onboarding-container">
            <AdmissaoStepper />

            <form className="onboarding-form">
                <TextField
                    label="Código do Banco"
                    name="bancoCodigo"
                    value={banco.bancoCodigo}
                    onChange={handleChange}
                    onBlur={handleBancoBlur}
                    fullWidth
                    autoFocus
                />
                <TextField
                    label="Nome do Banco"
                    name="bancoNome"
                    value={banco.bancoNome}
                    onChange={handleChange}
                    fullWidth
                    sx={{ mb: 1 }}
                />
                <TextField
                    label="Agência"
                    name="agencia"
                    value={banco.agencia}
                    onChange={handleChange}
                    fullWidth
                    sx={{ mb: 1 }}
                />
                <TextField
                    label="Número da Conta"
                    name="conta"
                    value={banco.conta}
                    onChange={handleChange}
                    fullWidth
                    sx={{ mb: 1 }}
                />
                <TextField
                    label="Dígito da conta"
                    name="digitoConta"
                    value={banco.digitoConta}
                    onChange={handleChange}
                    fullWidth
                    sx={{ mb: 1 }}
                />
                <TextField
                    select
                    label="Tipo da Conta"
                    name="tipoConta"
                    value={banco.tipoConta}
                    onChange={handleChange}
                    fullWidth
                    sx={{ mb: 1 }}
                >
                    {tipoContaOptions.map((option) => (
                        <MenuItem key={option.value} value={option.value}>
                            {option.label}
                        </MenuItem>
                    ))}
                </TextField>
                <TextField
                    label="Chave Pix"
                    name="chavePix"
                    value={banco.chavePix}
                    onChange={handleChange}
                    fullWidth
                    sx={{ mb: 1 }}
                />
                <TextField
                    select
                    label="Forma Pagamento"
                    name="formaPagamento"
                    value={banco.formaPagamento}
                    onChange={handleChange}
                    fullWidth
                    sx={{ mb: 1 }}
                >
                    {formaPagamentoOptions.map((option) => (
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
                    {saving ? "Salvando..." : "Continuar"}
                </Button>
            </form>
        </div>
    );
}
