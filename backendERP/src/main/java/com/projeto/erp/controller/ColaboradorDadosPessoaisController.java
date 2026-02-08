package com.projeto.erp.controller;

import com.projeto.erp.dtos.*;
import com.projeto.erp.service.ColaboradorDadosPessoaisService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/colaborador/{colaboradorId}/dados-pessoais")
public class ColaboradorDadosPessoaisController {

    private final ColaboradorDadosPessoaisService dadosPessoaisService;

    public ColaboradorDadosPessoaisController(ColaboradorDadosPessoaisService dadosPessoaisService) {
        this.dadosPessoaisService = dadosPessoaisService;
    }


    @PreAuthorize("hasAnyAuthority('COLABORADOR_READ', 'ONBOARDING_READ')")
    @GetMapping
    public ResponseEntity<ColaboradorDadosPessoaisResponse> obter(
            @PathVariable Long colaboradorId
    ) {
        return ResponseEntity.ok(dadosPessoaisService.obterPorColaborador(colaboradorId));
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PostMapping
    public ColaboradorDadosPessoaisResponse criar(@PathVariable Long colaboradorId, @RequestBody ColaboradorDadosPessoaisRequest dto) {
        return dadosPessoaisService.cadastrar(colaboradorId, dto);
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PutMapping
    public ResponseEntity<ColaboradorDadosPessoaisResponse> atualizar(@PathVariable Long colaboradorId, @RequestBody ColaboradorDadosPessoaisRequest
            dto){
        return ResponseEntity.ok(dadosPessoaisService.cadastrar(colaboradorId, dto));
    }

}
