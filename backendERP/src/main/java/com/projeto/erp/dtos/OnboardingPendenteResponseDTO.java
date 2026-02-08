package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.OnboardingValidacaoStep;
import java.util.Map;

public record OnboardingPendenteResponseDTO(
        Long colaboradorId,
        Map<OnboardingValidacaoStep, OnboardingValidacoesResponseDTO> validacoes
) {}