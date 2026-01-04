package com.projeto.erp.controller;

import com.projeto.erp.dtos.UsuarioDTORequest;
import com.projeto.erp.dtos.UsuarioDTOResponse;
import com.projeto.erp.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {


    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('USUARIO_VIEW')")
    @GetMapping
    public ResponseEntity<List<UsuarioDTOResponse>> listar(){
        return ResponseEntity.ok(service.listar());
    }

    @PreAuthorize("hasAuthority('USUARIO_VIEW')")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTOResponse> obter(@PathVariable Long id) {
        return ResponseEntity.ok(service.obter(id));
    }

    @PreAuthorize("hasAuthority('USUARIO_CREATE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioDTOResponse cadastrar(@RequestBody UsuarioDTORequest dto) {
        return service.cadastrar(dto);
    }

    @PreAuthorize("hasAuthority('USUARIO_UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTOResponse> atualizar(
            @PathVariable Long id,
            @RequestBody UsuarioDTORequest dto) {

        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @PreAuthorize("hasAuthority('USUARIO_DELETE')")
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }


}
