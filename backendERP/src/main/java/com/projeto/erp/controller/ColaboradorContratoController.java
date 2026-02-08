package com.projeto.erp.controller;

import com.projeto.erp.dtos.*;
import com.projeto.erp.service.ColaboradorContratoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/colaborador/{colaboradorId}/contrato")
public class ColaboradorContratoController {

    private final ColaboradorContratoService service;

    public ColaboradorContratoController(ColaboradorContratoService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('COLABORADOR_READ')")
    @GetMapping
    public ResponseEntity<ColaboradorContratoDTOResponse> obter(@PathVariable Long colaboradorId){
        return ResponseEntity.ok(service.obter(colaboradorId));
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PostMapping
    public ColaboradorContratoDTOResponse criar(@RequestBody ColaboradorContratoCreateDTO dto) {
        return service.criar(dto);
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PutMapping
    public ResponseEntity<ColaboradorContratoDTOResponse> atualizar(@PathVariable Long colaboradorId, @RequestBody ColaboradorContratoUpdateDTO dto){
        return ResponseEntity.ok(service.atualizar(colaboradorId, dto));
    }


}
