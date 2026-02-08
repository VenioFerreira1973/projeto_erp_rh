package com.projeto.erp.controller;

import com.projeto.erp.dtos.ColaboradorCreateDTOResponse;
import com.projeto.erp.dtos.UsuarioCreateDTORequest;
import com.projeto.erp.dtos.ColaboradorDTOResponse;
import com.projeto.erp.dtos.UsuarioUpdateDTO;
import com.projeto.erp.enumeracoes.OnboardingStep;
import com.projeto.erp.service.ColaboradorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    private final ColaboradorService service;

    public ColaboradorController(ColaboradorService service) {
        this.service = service;
    }


    @PreAuthorize("hasAuthority('COLABORADOR_READ')")
    @GetMapping
    public ResponseEntity<List<ColaboradorDTOResponse>> listarCandidatos() {
        return ResponseEntity.ok(service.listarCandidatos());
    }

    @PreAuthorize("hasAuthority('COLABORADOR_READ')")
    @GetMapping("/listar")
    public ResponseEntity<List<ColaboradorDTOResponse>> listarColaboradores() {
        return ResponseEntity.ok(service.listarColaboradores());
    }

    @PreAuthorize("hasAuthority('COLABORADOR_READ')")
    @GetMapping("/status")
    public ResponseEntity<List<ColaboradorDTOResponse>> listarPorStatus(
            @RequestParam OnboardingStep onboardingStep) {

        return ResponseEntity.ok(
                service.listarPorOnboardingStep(onboardingStep)
        );
    }

    @PreAuthorize("hasAuthority('COLABORADOR_READ')")
    @GetMapping("/{id}")
    public ResponseEntity<ColaboradorDTOResponse> obter(@PathVariable Long id){
        return ResponseEntity.ok(service.obter(id));
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PostMapping
    public ColaboradorCreateDTOResponse criar(@RequestBody UsuarioCreateDTORequest dto) {
        return service.cadastrar(dto);
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<ColaboradorDTOResponse> atualizar(@PathVariable Long id, @RequestBody UsuarioUpdateDTO
            dto){
        return ResponseEntity.ok(service.atualizarEmail(id, dto));
    }

}
