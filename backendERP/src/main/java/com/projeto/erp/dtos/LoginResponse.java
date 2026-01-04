package com.projeto.erp.dtos;

public record LoginResponse(
        String token,
        UsuarioSecurityResponse usuarioSecurityResponse
) {}