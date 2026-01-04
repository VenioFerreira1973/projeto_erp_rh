package com.projeto.erp.controller;

import com.projeto.erp.dtos.DepartamentoDTO;
import com.projeto.erp.service.DepartamentoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/departamentos")
public class DepartamentoController {

    private final DepartamentoService service;

    public DepartamentoController(DepartamentoService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('PERFIL_ADMIN')")
    @GetMapping
    public List<DepartamentoDTO> listar() {
        return service.listar();
    }
}
