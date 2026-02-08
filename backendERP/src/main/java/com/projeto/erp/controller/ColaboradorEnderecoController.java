package com.projeto.erp.controller;

import com.projeto.erp.dtos.ColaboradorEnderecoRequest;
import com.projeto.erp.dtos.ColaboradorEnderecoResponse;
import com.projeto.erp.service.ColaboradorEnderecoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/colaborador/{colaboradorId}/endereco")
public class ColaboradorEnderecoController {

    private final ColaboradorEnderecoService enderecoService;

    public ColaboradorEnderecoController(ColaboradorEnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }


    @PreAuthorize("hasAnyAuthority('COLABORADOR_READ', 'ONBOARDING_READ')")
    @GetMapping
    public ResponseEntity<ColaboradorEnderecoResponse> obter(
            @PathVariable Long colaboradorId
    ) {
        return ResponseEntity.ok(enderecoService.obterPorColaborador(colaboradorId));
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PostMapping
    public ColaboradorEnderecoResponse criar(@PathVariable Long colaboradorId, @RequestBody ColaboradorEnderecoRequest dto) {
        return enderecoService.cadastrar(colaboradorId, dto);
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PutMapping
    public ResponseEntity<ColaboradorEnderecoResponse> atualizar(@PathVariable Long colaboradorId, @RequestBody ColaboradorEnderecoRequest
            dto){
        return ResponseEntity.ok(enderecoService.cadastrar(colaboradorId, dto));
    }

}
