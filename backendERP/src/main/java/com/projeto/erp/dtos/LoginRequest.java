package com.projeto.erp.dtos;

public record LoginRequest(
        String login,
        String senha
) {}