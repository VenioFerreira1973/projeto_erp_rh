package com.projeto.erp.controller;

import com.projeto.erp.dtos.*;
import com.projeto.erp.service.PrimeiroAcessoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/primeiro-acesso")
public class PrimeiroAcessoController {

    private final PrimeiroAcessoService primeiroAcessoService;

    public PrimeiroAcessoController(PrimeiroAcessoService primeiroAcessoService) {
        this.primeiroAcessoService = primeiroAcessoService;
    }

    @PostMapping("/finalizar")
    public ResponseEntity<LoginResponse> finalizarPrimeiroAcesso(
            @RequestBody FinalizarPrimeiroAcessoRequest request,
            Authentication authentication
    ) {
        LoginResponse response =
                primeiroAcessoService.definirNovaSenha(authentication.getName(), request.novaSenha());

        return ResponseEntity.ok(response);
    }
}

