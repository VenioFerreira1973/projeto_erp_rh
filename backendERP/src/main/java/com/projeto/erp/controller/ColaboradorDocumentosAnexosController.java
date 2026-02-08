package com.projeto.erp.controller;

import com.projeto.erp.dtos.ColaboradorDocumentosAnexosRequestDTO;
import com.projeto.erp.dtos.ColaboradorDocumentosAnexosResponseDTO;
import com.projeto.erp.dtos.DocumentosAnexosComObservacaoResponseDTO;
import com.projeto.erp.service.ColaboradorDocumentosAnexosService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colaborador/{colaboradorId}/documentos-anexos")
public class ColaboradorDocumentosAnexosController {

    private final ColaboradorDocumentosAnexosService documentosAnexosService;

    public ColaboradorDocumentosAnexosController(ColaboradorDocumentosAnexosService documentosAnexosService) {
        this.documentosAnexosService = documentosAnexosService;
    }

    @PreAuthorize("hasAuthority('COLABORADOR_READ')")
    @GetMapping
    public ResponseEntity<DocumentosAnexosComObservacaoResponseDTO> obter(
            @PathVariable Long colaboradorId
    ) {
        return ResponseEntity.ok(documentosAnexosService.obterPorColaborador(colaboradorId));
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PostMapping
    public ColaboradorDocumentosAnexosResponseDTO criar(
            @PathVariable Long colaboradorId,
            @RequestBody ColaboradorDocumentosAnexosRequestDTO dto
    ) {
        return documentosAnexosService.cadastrar(colaboradorId, dto);
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PutMapping
    public ResponseEntity<ColaboradorDocumentosAnexosResponseDTO> atualizar(
            @PathVariable Long colaboradorId,
            @RequestBody ColaboradorDocumentosAnexosRequestDTO dto
    ){
        return ResponseEntity.ok(documentosAnexosService.atualizar(colaboradorId, dto));
    }
}
