package com.projeto.erp.controller;

import com.projeto.erp.dtos.*;
import com.projeto.erp.service.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/onboarding/corrigir")
public class OnboardingCorrigirController {

    private final ColaboradorDadosPessoaisService dadosPessoaisService;
    private final ColaboradorEnderecoService enderecoService;
    private final ColaboradorDadosBancariosService dadosBancariosService;
    private final ColaboradorDependentesService dependentesService;
    private final ColaboradorDocumentosService documentosService;
    private final ColaboradorDocumentosAnexosService documentosAnexosService;

    public OnboardingCorrigirController(ColaboradorDadosPessoaisService dadosPessoaisService,
                                        ColaboradorEnderecoService enderecoService,
                                        ColaboradorDadosBancariosService dadosBancariosService,
                                        ColaboradorDependentesService dependentesService,
                                        ColaboradorDocumentosService documentosService,
                                        ColaboradorDocumentosAnexosService documentosAnexosService) {
        this.dadosPessoaisService = dadosPessoaisService;
        this.enderecoService = enderecoService;
        this.dadosBancariosService = dadosBancariosService;
        this.dependentesService = dependentesService;
        this.documentosService = documentosService;
        this.documentosAnexosService = documentosAnexosService;
    }

    @PreAuthorize("hasAuthority('ONBOARDING_WRITE')")
    @PutMapping("/dados-pessoais")
    public ResponseEntity<ColaboradorDadosPessoaisResponse> corrigirDadosPessoais(
            @RequestBody ColaboradorDadosPessoaisRequest request
    ) {
        return ResponseEntity.ok(dadosPessoaisService.corrigir(request));
    }

    @PreAuthorize("hasAuthority('ONBOARDING_WRITE')")
    @PutMapping("/dependentes")
    public ResponseEntity<List<ColaboradorDependentesResponseDTO>> corrigirDadosPessoais(
            @RequestBody List<ColaboradorDependentesRequestDTO> request
    ) {
        return ResponseEntity.ok(dependentesService.corrigir(request));
    }

    @PreAuthorize("hasAuthority('ONBOARDING_WRITE')")
    @PutMapping("/endereco")
    public ResponseEntity<ColaboradorEnderecoResponse> corrigirEndereco(
            @RequestBody ColaboradorEnderecoRequest request
    ) {
        return ResponseEntity.ok(enderecoService.corrigir(request));
    }

    @PreAuthorize("hasAuthority('ONBOARDING_WRITE')")
    @PutMapping("/dados-bancarios")
    public ResponseEntity<ColaboradorDadosBancariosResponseDTO> corrigirDadosPessoais(
            @RequestBody ColaboradorDadosBancariosRequestDTO request
    ) {
        return ResponseEntity.ok(dadosBancariosService.corrigir(request));
    }

    @PreAuthorize("hasAuthority('ONBOARDING_WRITE')")
    @PutMapping("/documentos")
    public ResponseEntity<ColaboradorDocumentosResponseDTO> corrigirDadosPessoais(
            @RequestBody ColaboradorDocumentosRequestDTO request
    ) {
        return ResponseEntity.ok(documentosService.corrigir(request));
    }

    @PreAuthorize("hasAuthority('ONBOARDING_WRITE')")
    @PutMapping(value = "/documentos-anexos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ColaboradorDocumentosAnexosResponseDTO> corrigirDocumentosAnexos(
            @RequestPart("tipoDocumentoAnexo") String tipoDocumentoAnexo,
            @RequestPart("arquivo") MultipartFile arquivo
    ) {
        return ResponseEntity.ok(
                documentosAnexosService.corrigir(tipoDocumentoAnexo, arquivo)
        );
    }
}
