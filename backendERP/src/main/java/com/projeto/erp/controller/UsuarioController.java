package com.projeto.erp.controller;

import com.projeto.erp.dtos.UsuarioDTOResponse;
import com.projeto.erp.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('COLABORADOR_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTOResponse> obter(@PathVariable Long id){
        return ResponseEntity.ok(service.obter(id));
    }
}
