package com.projeto.erp.controller;

import com.projeto.erp.dtos.*;
import com.projeto.erp.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/rh/onboarding/{colaboradorId}")
public class RHOnboardingController {

    private final ColaboradorDadosPessoaisService dadosPessoaisService;
    private final ColaboradorDadosBancariosService dadosBancariosService;
    private final ColaboradorDependentesService dependentesService;
    private final ColaboradorDocumentosService documentosService;
    private final ColaboradorDocumentosAnexosService documentosAnexosService;
    private final ColaboradorEnderecoService enderecoService;

    public RHOnboardingController(ColaboradorDadosPessoaisService dadosPessoaisService, ColaboradorDadosBancariosService dadosBancariosService, ColaboradorDependentesService dependentesService, ColaboradorDocumentosService documentosService, ColaboradorDocumentosAnexosService documentosAnexosService, ColaboradorEnderecoService enderecoService) {
        this.dadosPessoaisService = dadosPessoaisService;
        this.dependentesService = dependentesService;
        this.dadosBancariosService = dadosBancariosService;
        this.documentosService = documentosService;
        this.documentosAnexosService = documentosAnexosService;
        this.enderecoService = enderecoService;
    }

    @PreAuthorize("hasAnyAuthority('ONBOARDING_READ')")
    @GetMapping("/dados-pessoais")
    public ResponseEntity<ColaboradorDadosPessoaisResponse> obterDadosPessoais(
            @PathVariable Long colaboradorId
    ) {
        return ResponseEntity.ok(dadosPessoaisService.obterPorColaborador(colaboradorId));
    }

    @PreAuthorize("hasAnyAuthority('ONBOARDING_READ')")
    @GetMapping("/dependentes")
    public ResponseEntity<DependentesComObservacaoResponseDTO> obterDependentes(
            @PathVariable Long colaboradorId
    ) {
        return ResponseEntity.ok(dependentesService.obterPorColaborador(colaboradorId));
    }

    @PreAuthorize("hasAnyAuthority('COLABORADOR_READ', 'ONBOARDING_READ')")
    @GetMapping("/endereco")
    public ResponseEntity<ColaboradorEnderecoResponse> obterEndereco(
            @PathVariable Long colaboradorId
    ) {
        return ResponseEntity.ok(enderecoService.obterPorColaborador(colaboradorId));
    }

    @PreAuthorize("hasAnyAuthority('ONBOARDING_READ')")
    @GetMapping("/dados-bancarios")
    public ResponseEntity<ColaboradorDadosBancariosResponseDTO> obterDadosBancarios(
            @PathVariable Long colaboradorId
    ) {
        return ResponseEntity.ok(dadosBancariosService.obterPorColaborador(colaboradorId));
    }

    @PreAuthorize("hasAnyAuthority('ONBOARDING_READ')")
    @GetMapping("/documentos")
    public ResponseEntity<ColaboradorDocumentosResponseDTO> obterDocumentos(
            @PathVariable Long colaboradorId
    ) {
        return ResponseEntity.ok(documentosService.obterPorColaborador(colaboradorId));
    }

    @PreAuthorize("hasAnyAuthority('ONBOARDING_READ')")
    @GetMapping("/documentos-anexos")
    public ResponseEntity<DocumentosAnexosComObservacaoResponseDTO> obterDocumentosAnexos(
            @PathVariable Long colaboradorId
    ) {
        return ResponseEntity.ok(documentosAnexosService.obterPorColaborador(colaboradorId));
    }
}
