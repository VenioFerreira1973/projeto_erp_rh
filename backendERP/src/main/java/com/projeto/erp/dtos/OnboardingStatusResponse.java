package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.OnboardingStep;

public record OnboardingStatusResponse(
        boolean primeiroAcesso,
        OnboardingStep step
) {}
