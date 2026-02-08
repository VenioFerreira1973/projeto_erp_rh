package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.*;

import java.time.Instant;

public record ColaboradorLotacaoDTOResponse(
        Long id,

        Long colaboradorId,
        String colaboradorNome,
        String matricula,

        String cargo,
        String departamento,
        String gestor,
        String centroCusto,

        String cargoCbo,
        NivelCargo nivelCargo,

        Instant dataCriacao,
        Instant dataAlteracao
) {}
