package com.projeto.erp.controller;

import com.projeto.erp.dtos.*;
import com.projeto.erp.service.OnboardingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {

    private final OnboardingService service;

    public OnboardingController(OnboardingService service) {
        this.service = service;
    }

    @GetMapping("/status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OnboardingStatusResponse> status() {

        return ResponseEntity.ok(service.obterStatus());
    }

    @PreAuthorize("hasAuthority('ONBOARDING_READ')")
    @GetMapping("/usuario")
    public ResponseEntity<UsuarioDTOResponse> buscarUsuario() {

        return ResponseEntity.ok(service.buscarUsuario());
    }

    @PreAuthorize("hasAuthority('ONBOARDING_READ')")
    @GetMapping("/colaborador")
    public ResponseEntity<ColaboradorDTOResponse> buscarColaborador() {
        return ResponseEntity.ok(service.buscarColaborador());
    }

    @PreAuthorize("hasAuthority('ONBOARDING_READ')")
    @GetMapping("/dados-pessoais")
    public ResponseEntity<ColaboradorDadosPessoaisResponse> buscarDadosPessoais() {
        return ResponseEntity.ok(service.buscarDadosPessoais());
    }

    @PreAuthorize("hasAuthority('ONBOARDING_READ')")
    @GetMapping("/dependentes")
    public ResponseEntity<DependentesComObservacaoResponseDTO> buscarDependentes() {
        return ResponseEntity.ok(service.buscarDependentes());
    }

    @PreAuthorize("hasAuthority('ONBOARDING_READ')")
    @GetMapping("/endereco")
    public ResponseEntity<ColaboradorEnderecoResponse> buscarEndereco() {
        return ResponseEntity.ok(service.buscarEndereco());
    }

    @PreAuthorize("hasAuthority('ONBOARDING_READ')")
    @GetMapping("/dados-bancarios")
    public ResponseEntity<ColaboradorDadosBancariosResponseDTO> buscarDadosBancarios() {
        return ResponseEntity.ok(service.buscarDadosBancarios());
    }

    @PreAuthorize("hasAuthority('ONBOARDING_READ')")
    @GetMapping("/documentos")
    public ResponseEntity<ColaboradorDocumentosResponseDTO> buscarDocumentos() {
        return ResponseEntity.ok(service.buscarDocumentos());
    }

    @PreAuthorize("hasAuthority('ONBOARDING_READ')")
    @GetMapping("/documentos-anexos")
    public ResponseEntity<DocumentosAnexosComObservacaoResponseDTO> buscarDocumentosAnexos() {
        return ResponseEntity.ok(service.buscarDocumentosAnexos());
    }

    @PostMapping("/analisar")
    @PreAuthorize("isAuthenticated()")
    public void analisar() {
        service.enviarParaAnalise();
    }

}
