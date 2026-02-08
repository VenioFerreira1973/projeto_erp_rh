package com.projeto.erp.controller;

import com.projeto.erp.dtos.ColaboradorDependentesRequestDTO;
import com.projeto.erp.dtos.ColaboradorDependentesResponseDTO;
import com.projeto.erp.dtos.DependentesComObservacaoResponseDTO;
import com.projeto.erp.service.ColaboradorDependentesService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colaborador/{colaboradorId}/dependentes")
public class ColaboradorDependentesController {

    private final ColaboradorDependentesService dependentesService;

    public ColaboradorDependentesController(ColaboradorDependentesService dependentesService) {
        this.dependentesService = dependentesService;

    }

    @PreAuthorize("hasAuthority('COLABORADOR_READ')")
    @GetMapping
    public DependentesComObservacaoResponseDTO listarDependentes() {
        return dependentesService.listarDependentes();
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PostMapping
    public List<ColaboradorDependentesResponseDTO> adicionarDependentes(
            @RequestBody List<ColaboradorDependentesRequestDTO> request
    ) {
        return dependentesService.adicionarDependentes(request);
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PutMapping("/{id}")
    public ColaboradorDependentesResponseDTO atualizarDependente(
            @PathVariable Long id,
            @RequestBody ColaboradorDependentesRequestDTO request
    ) {
        return dependentesService.atualizarDependente(id, request);
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @DeleteMapping("/{id}")
    public void removerDependente(@PathVariable Long id) {
        dependentesService.removerDependente(id);
    }

}
