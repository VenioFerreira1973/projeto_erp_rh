package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.MotivoAlteracaoSalario;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalarioDTO(
        BigDecimal valor,
        LocalDate dataInicio,
        MotivoAlteracaoSalario motivo
) {}
