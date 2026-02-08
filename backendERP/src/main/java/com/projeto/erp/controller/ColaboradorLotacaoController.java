package com.projeto.erp.controller;

import com.projeto.erp.dtos.*;
import com.projeto.erp.service.ColaboradorLotacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/colaborador/{colaboradorId}/lotacao")
public class ColaboradorLotacaoController {

    private final ColaboradorLotacaoService service;

    public ColaboradorLotacaoController(ColaboradorLotacaoService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('COLABORADOR_READ')")
    @GetMapping
    public ResponseEntity<ColaboradorLotacaoDTOResponse> obter(@PathVariable Long colaboradorId){
        return ResponseEntity.ok(service.obter(colaboradorId));
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PostMapping
    public ColaboradorLotacaoDTOResponse criar(@RequestBody ColaboradorLotacaoCreateDTO dto) {
        return service.criar(dto);
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PutMapping
    public ResponseEntity<ColaboradorLotacaoDTOResponse> atualizar(@PathVariable Long colaboradorId, @RequestBody ColaboradorLotacaoUpdateDTO dto){
        return ResponseEntity.ok(service.atualizar(colaboradorId, dto));
    }


}
