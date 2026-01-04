package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FuncionarioDTORequest(
        String nome,
        BigDecimal salario,
        LocalDate dataAdmissao,
        Status status,
        Long cargoId,
        Long departamentoId,
        boolean gerente

) {}