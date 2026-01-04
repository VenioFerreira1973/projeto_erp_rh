package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FuncionarioDTOResponse(
        Long id,
        String nome,
        BigDecimal salario,
        LocalDate dataAdmissao,
        Status status,
        boolean gerente,
        String usuarioLogin,
        CargoDTO cargoDTO,
        DepartamentoDTO departamentoDTO

) {}
