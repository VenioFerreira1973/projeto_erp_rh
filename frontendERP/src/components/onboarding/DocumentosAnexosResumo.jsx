import {
  Dialog,
  DialogContent,
  DialogActions,
  Button,
  Typography,
  Box
} from "@mui/material";
import RotateRightIcon from "@mui/icons-material/RotateRight";
import VisibilityIcon from "@mui/icons-material/Visibility";
import UploadFileIcon from "@mui/icons-material/UploadFile";
import { onboardingCorrigirService } from "../../services/onboardingCorrigirService";
import { useEffect, useState, useRef } from "react";
import { formatarDataBR } from "../../utils/dateUtils";

export default function DocumentosAnexosResumo({
  documentosAnexos,
  statusValidacao,
  observacao,
  onSalvo,
  permitirCorrecao,
}) {
  const podeEditar = permitirCorrecao && statusValidacao === "REPROVADO";
  const [form, setForm] = useState([]);
  const [open, setOpen] = useState(false);
  const [selectedImg, setSelectedImg] = useState("");
  const [rotation, setRotation] = useState(0);
  const [scale, setScale] = useState(1);
  const [isDragging, setIsDragging] = useState(false);
  const [position, setPosition] = useState({ x: 0, y: 0 });
  const [startPos, setStartPos] = useState({ x: 0, y: 0 });
  const [arquivoNovo, setArquivoNovo] = useState(null);
  const [tipoSelecionado, setTipoSelecionado] = useState(null);
  const [editando, setEditando] = useState(false);

  const visualizarBtnRef = useRef(null);

  useEffect(() => {
    if (Array.isArray(documentosAnexos)) {
      setForm(
        documentosAnexos.map((doc) => ({
          ...doc,
          dataValidade: doc.dataValidade ?? "",
          statusDocumento: doc.statusDocumento ?? "",
          tipoDocumentoAnexo: doc.tipoDocumentoAnexo ?? ""
        }))
      );
    } else {
      setForm([]);
    }
  }, [documentosAnexos]);

  const handleOpen = (url) => {
    setRotation(0);
    setScale(1);
    setPosition({ x: 0, y: 0 });
    setSelectedImg(url);
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setIsDragging(false);

    setTimeout(() => {
      visualizarBtnRef.current?.focus();
    }, 0);
  };

  const handleMouseDown = (e) => {
    e.preventDefault();
    setIsDragging(true);
    setStartPos({
      x: e.clientX - position.x,
      y: e.clientY - position.y,
    });
  };

  const handleMouseMove = (e) => {
    if (!isDragging) return;
    setPosition({
      x: e.clientX - startPos.x,
      y: e.clientY - startPos.y,
    });
  };

  const handleMouseUp = () => {
    setIsDragging(false);
  };

  const handleWheel = (e) => {
    e.preventDefault();

    const zoomSpeed = 0.1;
    const direction = e.deltaY < 0 ? 1 : -1;

    const newScale = Math.min(
      Math.max(scale + direction * zoomSpeed, 0.5),
      3
    );

    if (newScale === scale) return;

    const rect = e.currentTarget.getBoundingClientRect();

    const mouseX = e.clientX - rect.left;
    const mouseY = e.clientY - rect.top;

    const scaleRatio = newScale / scale;

    setPosition((prev) => ({
      x: mouseX - (mouseX - prev.x) * scaleRatio,
      y: mouseY - (mouseY - prev.y) * scaleRatio,
    }));

    setScale(newScale);
  };


  const handleRotate = () => {
    setRotation((prev) => prev + 90);
  };

  const handleResetPosition = () => {
    setPosition({ x: 0, y: 0 });
    setScale(1);
    setRotation(0);
  };

  const handleSalvar = async () => {
    if (!arquivoNovo || !tipoSelecionado) return;

    const formData = new FormData();
    formData.append("tipoDocumentoAnexo", tipoSelecionado);
    formData.append("arquivo", arquivoNovo);

    await onboardingCorrigirService.corrigirDocumentosAnexos(formData);

    setArquivoNovo(null);
    setTipoSelecionado(null);
    setEditando(false);
    onSalvo("DOCUMENTOS_ANEXOS");
  };

  const handleSelecionarArquivo = (tipo, arquivo) => {
    setArquivoNovo(arquivo);
    setTipoSelecionado(tipo);
  };


  if (!documentosAnexos || documentosAnexos.length === 0) {
    return <p>Nenhum anexo encontrado.</p>;
  }

  return (
    <div className="card">
      <Typography variant="h6" gutterBottom>
        Documentos Anexados
      </Typography>

      {podeEditar && observacao && (
        <div style={{ background: "#fdecea", padding: 12, marginBottom: 16 }}>
          <strong>Observação do RH:</strong>
          <p>{observacao}</p>
        </div>
      )}

      <Box component="ul" sx={{ p: 0, listStyle: "none" }}>
        {documentosAnexos.map((doc) => (
          <Box
            component="li"
            key={doc.id}
            sx={{ mb: 2, borderBottom: "1px solid #eee", pb: 1 }}
          >
            <Typography variant="caption" display="block">
              <strong>{doc.tipoDocumentoAnexo}</strong> —{" "}
              {doc.statusDocumento}
            </Typography>
            <Typography variant="caption" display="block">
              Validade:{" "}
              {doc.dataValidade
                ? formatarDataBR(doc.dataValidade)
                : "Não informado"}
            </Typography>

            <Button
              ref={visualizarBtnRef}
              size="small"
              startIcon={<VisibilityIcon />}
              onClick={() => handleOpen(doc.arquivoUrl)}
              sx={{ mt: 0.5, textTransform: "none" }}
            >
              Visualizar
            </Button>

            {podeEditar && editando && (
              <Button
                component="label"
                size="small"
                startIcon={<UploadFileIcon />}
                sx={{ ml: 1 }}
              >
                Substituir
                <input
                  type="file"
                  hidden
                  onChange={(e) =>
                    handleSelecionarArquivo(
                      doc.tipoDocumentoAnexo,
                      e.target.files[0]
                    )
                  }
                />
              </Button>
            )}
          </Box>
        ))}
      </Box>

      {podeEditar && !editando && (
        <Button variant="outlined" color="warning" onClick={() => setEditando(true)}>
          Corrigir dados
        </Button>
      )}

      {podeEditar && editando && arquivoNovo && (
        <Button variant="contained" color="warning" onClick={handleSalvar}>
          Salvar correção
        </Button>
      )}

      <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth>
        <DialogContent
          sx={{
            textAlign: "center",
            backgroundColor: "#222",
            minHeight: "60vh",
            overflow: "hidden",
          }}
          onWheel={handleWheel}
          onMouseMove={handleMouseMove}
          onMouseUp={handleMouseUp}
          onMouseLeave={handleMouseUp}
        >
          <Box
            sx={{
              display: "inline-block",
              transform: `
                translate(${position.x}px, ${position.y}px)
                scale(${scale})
              `,
              transformOrigin: "0 0",
            }}
          >
            <Box
              component="img"
              key={selectedImg}
              src={selectedImg}
              onMouseDown={handleMouseDown}
              draggable={false}
              alt="Documento"
              sx={{
                maxWidth: "100%",
                maxHeight: "70vh",
                cursor: scale > 1 ? "grab" : "zoom-in",
                transform: `rotate(${rotation}deg)`,
                transformOrigin: "center center",
                display: "block",
              }}
            />
          </Box>
        </DialogContent>

        <DialogActions>
          <Button startIcon={<RotateRightIcon />} onClick={handleRotate}>
            Girar
          </Button>
          <Button onClick={handleResetPosition}>Centralizar</Button>
          <Button onClick={handleClose}>Fechar</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
