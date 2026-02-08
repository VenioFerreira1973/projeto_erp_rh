package com.projeto.erp.dtos;

import java.util.List;


public record OnboardingValidacoesLoteRequestDTO(
        List<OnboardingValidacaoLoteDTO> validacoes
) {}