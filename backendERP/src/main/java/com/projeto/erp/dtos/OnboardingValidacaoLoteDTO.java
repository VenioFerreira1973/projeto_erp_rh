package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.OnboardingValidacaoStep;
import com.projeto.erp.enumeracoes.StatusValidacao;

public record OnboardingValidacaoLoteDTO(
        OnboardingValidacaoStep step,
        StatusValidacao status,
        String observacao
) {}
