package com.projeto.erp.controller;

import com.projeto.erp.dtos.FuncionarioDTORequest;
import com.projeto.erp.dtos.FuncionarioDTOResponse;
import com.projeto.erp.repository.FuncionarioRepository;
import com.projeto.erp.service.FuncionarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final FuncionarioService service;

    public FuncionarioController(FuncionarioService service, FuncionarioRepository repository) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('FUNCIONARIO_READ')")
    @GetMapping
    public ResponseEntity<List<FuncionarioDTOResponse>> listar(){
        return ResponseEntity.ok(service.listar());
    }

    @PreAuthorize("hasAuthority('FUNCIONARIO_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioDTOResponse> obter(@PathVariable Long id){
        return ResponseEntity.ok(service.obter(id));
    }

    @PreAuthorize("hasAuthority('FUNCIONARIO_WRITE')")
    @PostMapping
    public FuncionarioDTOResponse criar(@RequestBody FuncionarioDTORequest dto) {
        return service.cadastrar(dto);
    }

    @PreAuthorize("hasAuthority('FUNCIONARIO_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioDTOResponse> atualizar(@PathVariable Long id, @RequestBody FuncionarioDTORequest dto){
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @PreAuthorize("hasAuthority('FUNCIONARIO_ADMIN')")
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    @PreAuthorize("hasAuthority('FUNCIONARIO_READ')")
    @GetMapping("/departamento/{departamentoId}")
    public ResponseEntity<List<FuncionarioDTOResponse>> listarPorDepartamento(@PathVariable Long departamentoId) {
        return ResponseEntity.ok(service.listarPorDepartamento(departamentoId));
    }

    @PreAuthorize("hasAuthority('FUNCIONARIO_READ')")
    @GetMapping("/cargo/{cargoId}")
    public ResponseEntity<List<FuncionarioDTOResponse>> listarPorCargo(@PathVariable Long cargoId) {
        return ResponseEntity.ok(service.listarPorCargo(cargoId));
    }

    @PreAuthorize("hasAuthority('FUNCIONARIO_READ')")
    @GetMapping("/gerentes")
    public ResponseEntity<List<FuncionarioDTOResponse>> listarGerentes() {
        return ResponseEntity.ok(service.listarGerentes());
    }
}
