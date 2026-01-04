package com.projeto.erp.controller;

import com.projeto.erp.dtos.PermissaoDTO;
import com.projeto.erp.service.PermissaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissoes")
public class PermissaoController {

    private final PermissaoService service;

    public PermissaoController(PermissaoService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('PERFIL_ADMIN')")
    @GetMapping
    public ResponseEntity<List<PermissaoDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @PreAuthorize("hasAuthority('PERFIL_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PermissaoDTO> obter(@PathVariable Long id) {
        return ResponseEntity.ok(service.obter(id));
    }

    @PreAuthorize("hasAuthority('PERFIL_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PermissaoDTO cadastrar(@RequestBody PermissaoDTO permissaoDTO) {
        return service.cadastrar(permissaoDTO);
    }

    @PreAuthorize("hasAuthority('PERFIL_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PermissaoDTO> atualizar(
            @RequestBody PermissaoDTO dto,
            @PathVariable Long id) {

        PermissaoDTO atualizado = service.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @PreAuthorize("hasAuthority('PERFIL_ADMIN')")
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}