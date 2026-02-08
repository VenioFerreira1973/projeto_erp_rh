import { useEffect, useState } from "react";
import {
    Box,
    Typography,
    Paper,
    TextField,
    RadioGroup,
    FormControlLabel,
    Radio
} from "@mui/material";

export default function RHValidacaoCard({
    titulo,
    status,
    observacao,
    onChange,
    children
}) {
    const [localStatus, setLocalStatus] = useState(status);
    const [localObs, setLocalObs] = useState(observacao ?? "");

    useEffect(() => {
        setLocalStatus(status ?? "");
        setLocalObs(observacao ?? "");
    }, [status, observacao]);

    return (
        <Box sx={{ mt: 2, width: "100%", maxWidth: "100%" }}>
            <Paper sx={{ p: 2, mb: 2 }}>
                <Typography variant="h6">{titulo}</Typography>

                <Box sx={{ mt: 2 }}>
                    {children}
                </Box>

                <Box sx={{ mt: 2 }}>
                    <RadioGroup
                        row
                        value={localStatus ?? ""}
                        onChange={(e) => {
                            const novoStatus = e.target.value;
                            setLocalStatus(novoStatus);
                            onChange?.(novoStatus, localObs);
                        }}
                    >
                        <FormControlLabel value="APROVADO" control={<Radio />} label="Aprovado" />
                        <FormControlLabel value="REPROVADO" control={<Radio />} label="Reprovado" />
                    </RadioGroup>

                    {localStatus === "REPROVADO" && (
                        <TextField
                            label="Observação"
                            multiline
                            fullWidth
                            rows={3}
                            value={localObs ?? ""}
                            onChange={(e) => {
                                const obs = e.target.value;
                                setLocalObs(obs);
                                onChange?.(localStatus, obs);
                            }}
                            sx={{ mt: 2 }}
                        />
                    )}
                </Box>
            </Paper>
        </Box>
    );
}
