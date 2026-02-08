package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.OnboardingValidacaoStep;
import com.projeto.erp.enumeracoes.StatusValidacao;

import java.time.Instant;

public record OnboardingValidacoesResponseDTO(
        Long id,
        Long colaboradorId,
        String nomeColaborador,
        OnboardingValidacaoStep onboardingValidacaoStep,
        StatusValidacao statusValidacao,
        String observacao,
        String validadoPor,
        Instant validadoEm
) {}
