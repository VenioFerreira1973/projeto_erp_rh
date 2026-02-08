package com.projeto.erp.controller;

import com.projeto.erp.dtos.ColaboradorDocumentosRequestDTO;
import com.projeto.erp.dtos.ColaboradorDocumentosResponseDTO;
import com.projeto.erp.service.ColaboradorDocumentosService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/colaborador/{colaboradorId}/documentos")
public class ColaboradorDocumentosController {

    private final ColaboradorDocumentosService documentosService;

    public ColaboradorDocumentosController(ColaboradorDocumentosService dadosDocumentosService) {
        this.documentosService = dadosDocumentosService;
    }


    @PreAuthorize("hasAuthority('COLABORADOR_READ')")
    @GetMapping
    public ResponseEntity<ColaboradorDocumentosResponseDTO> obter(
            @PathVariable Long colaboradorId
    ) {
        return ResponseEntity.ok(documentosService.obterPorColaborador(colaboradorId));
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PostMapping
    public ColaboradorDocumentosResponseDTO criar(@PathVariable Long colaboradorId, @RequestBody ColaboradorDocumentosRequestDTO dto) {
        return documentosService.cadastrar(colaboradorId, dto);
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PutMapping
    public ResponseEntity<ColaboradorDocumentosResponseDTO> atualizar(@PathVariable Long colaboradorId, @RequestBody ColaboradorDocumentosRequestDTO
            dto){
        return ResponseEntity.ok(documentosService.cadastrar(colaboradorId, dto));
    }

}
