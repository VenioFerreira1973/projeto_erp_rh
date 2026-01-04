package com.projeto.erp.controller;

import com.projeto.erp.dtos.PerfilDTO;
import com.projeto.erp.service.PerfilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/perfis")
public class PerfilController {

    private final PerfilService service;

    public PerfilController(PerfilService service) {
        this.service = service;
    }

    @PreAuthorize("hasAuthority('PERFIL_ADMIN')")
    @GetMapping
    public List<PerfilDTO> listar(){
       return service.listar();
    }

    @PreAuthorize("hasAuthority('PERFIL_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PerfilDTO> obter(@PathVariable Long id){
        return ResponseEntity.ok(service.obter(id));
    }

    @PreAuthorize("hasAuthority('PERFIL_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PerfilDTO cadastrar(@RequestBody PerfilDTO perfil) {
        return service.cadastrar(perfil);
    }

    @PreAuthorize("hasAuthority('PERFIL_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PerfilDTO> atualizar(@RequestBody PerfilDTO dto,
                                                 @PathVariable Long id){
        PerfilDTO atualizado =  service.atualizar(id, dto);
        return  ResponseEntity.ok(atualizado);
    }

    @PreAuthorize("hasAuthority('PERFIL_ADMIN')")
    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
            service.deletar(id);
        }


}
