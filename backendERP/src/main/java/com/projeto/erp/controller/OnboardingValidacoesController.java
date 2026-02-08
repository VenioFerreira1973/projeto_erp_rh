package com.projeto.erp.controller;

import com.projeto.erp.dtos.*;
import com.projeto.erp.service.OnboardingValidacoesService;
import com.projeto.erp.service.UsuarioAutenticadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/onboarding/validacoes")
@PreAuthorize("hasAuthority('RH_ONBOARDING')")
public class OnboardingValidacoesController {

    private final OnboardingValidacoesService validacoesService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public OnboardingValidacoesController(
            OnboardingValidacoesService validacoesService,
            UsuarioAutenticadoService usuarioAutenticadoService
    ) {
        this.validacoesService = validacoesService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @PreAuthorize("hasAuthority('ONBOARDING_READ')")
    @GetMapping("/{colaboradorId}/validacoes")
    public List<OnboardingValidacoesResponseDTO> listarValidacoes(
            @PathVariable Long colaboradorId
    ) {
        return validacoesService.listarValidacoes(colaboradorId);
    }

    @PreAuthorize("hasAuthority('ONBOARDING_WRITE')")
    @PostMapping("/{colaboradorId}/validar-lote")
    public ResponseEntity<Void> validarOnboardingLote(
            @PathVariable Long colaboradorId,
            @RequestBody OnboardingValidacoesLoteRequestDTO request
    ) {
        Long rhId = usuarioAutenticadoService.getUsuario().getId();
        validacoesService.validarOnboardingLote(colaboradorId, request, rhId);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasAuthority('ONBOARDING_WRITE')")
    @PostMapping("/validacao")
    public ResponseEntity<OnboardingValidacoesResponseDTO> salvarValidacaoOnboarding(
            @RequestBody OnboardingValidacoesRequestDTO request
    ) {
        return ResponseEntity.ok(validacoesService.salvarValidacao(request));
    }

    @PreAuthorize("hasAuthority('ONBOARDING_READ')")
    @GetMapping("/validacoes/pendentes")
    public OnboardingPendenteResponseDTO getOnboardingPendente() {
        return validacoesService.buscarOnboardingPendente();
    }

    @PreAuthorize("hasAuthority('ONBOARDING_READ')")
    @GetMapping("/pendentes")
    public List<OnboardingValidacoesResponseDTO> listarColaboradoresPendentes() {
        return validacoesService.listarColaboradoresPendentes();
    }


    @PreAuthorize("hasAuthority('ONBOARDING_WRITE')")
    @PostMapping("/documentos")
    public ColaboradorDocumentosResponseDTO salvarDocumentos(@RequestBody ColaboradorDocumentosRequestDTO request) {
        return validacoesService.salvarDocumentos(request);
    }

    @PreAuthorize("hasAuthority('ONBOARDING_WRITE')")
    @PostMapping("/documentos-anexos")
    public List<ColaboradorDocumentosAnexosResponseDTO> salvarDocumentosAnexos(
            @RequestBody List<ColaboradorDocumentosAnexosRequestDTO> request
    ) {
        return validacoesService.salvarDocumentosAnexos(request);
    }

    @PreAuthorize("hasAuthority('ONBOARDING_WRITE')")
    @PostMapping("/dados-bancarios")
    public ColaboradorDadosBancariosResponseDTO salvarDadosBancarios(@RequestBody ColaboradorDadosBancariosRequestDTO request) {
        return validacoesService.salvarDadosBancarios(request);
    }

    @PreAuthorize("hasAuthority('ONBOARDING_WRITE')")
    @PostMapping("/endereco")
    public ColaboradorEnderecoResponse salvarEndereco(@RequestBody ColaboradorEnderecoRequest request) {
        return validacoesService.salvarEndereco(request);
    }

    @PreAuthorize("hasAuthority('ONBOARDING_WRITE')")
    @PostMapping("/dados-pessoais")
    public ColaboradorDadosPessoaisResponse salvarDadosPessoais(@RequestBody ColaboradorDadosPessoaisRequest request) {
        return validacoesService.salvarDadosPessoais(request);
    }

    @PreAuthorize("hasAuthority('ONBOARDING_WRITE')")
    @PostMapping("/dependentes")
    public List<ColaboradorDependentesResponseDTO> salvarDependentes(
            @RequestBody List<ColaboradorDependentesRequestDTO> request
    ) {
        return validacoesService.salvarDependentes(request);
    }


}
