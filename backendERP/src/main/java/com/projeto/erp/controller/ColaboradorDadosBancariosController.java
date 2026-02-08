package com.projeto.erp.controller;

import com.projeto.erp.dtos.ColaboradorDadosBancariosRequestDTO;
import com.projeto.erp.dtos.ColaboradorDadosBancariosResponseDTO;
import com.projeto.erp.service.ColaboradorDadosBancariosService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/colaborador/{colaboradorId}/dados-bacarios")
public class ColaboradorDadosBancariosController {

    private final ColaboradorDadosBancariosService dadosBancariosService;

    public ColaboradorDadosBancariosController(ColaboradorDadosBancariosService dadosBancariosService) {
        this.dadosBancariosService = dadosBancariosService;
    }


    @PreAuthorize("hasAuthority('COLABORADOR_READ')")
    @GetMapping
    public ResponseEntity<ColaboradorDadosBancariosResponseDTO> obter(
            @PathVariable Long colaboradorId
    ) {
        return ResponseEntity.ok(dadosBancariosService.obterPorColaborador(colaboradorId));
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PostMapping
    public ColaboradorDadosBancariosResponseDTO criar(@PathVariable Long colaboradorId, @RequestBody ColaboradorDadosBancariosRequestDTO dto) {
        return dadosBancariosService.cadastrar(colaboradorId, dto);
    }

    @PreAuthorize("hasAuthority('COLABORADOR_WRITE')")
    @PutMapping
    public ResponseEntity<ColaboradorDadosBancariosResponseDTO> atualizar(@PathVariable Long colaboradorId, @RequestBody ColaboradorDadosBancariosRequestDTO
            dto){
        return ResponseEntity.ok(dadosBancariosService.cadastrar(colaboradorId, dto));
    }

}
