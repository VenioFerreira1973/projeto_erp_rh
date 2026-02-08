package com.projeto.erp.dtos;

public record ColaboradorPendenteResponseDTO(
        Long colaboradorId,
        String nome,
        String onboarding_validacao_step
) {}

