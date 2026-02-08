package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.OnboardingValidacaoStep;
import com.projeto.erp.enumeracoes.StatusValidacao;


public record OnboardingValidacoesRequestDTO(
        Long colaboradorId,
        OnboardingValidacaoStep onboardingValidacaoStep,
        StatusValidacao statusValidacao,
        String observacao
) {}
