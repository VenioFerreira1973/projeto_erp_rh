package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.*;

import java.time.LocalDate;

public record ColaboradorContratoDTORequest(
        Long colaboradorId,
        Long empresaId,
        Long sindicatoId,

        TipoContrato tipoContrato,
        PrazoContrato prazoContrato,
        TipoJornada tipoJornada,
        Integer horasSemanais,

        TipoVinculo tipoVinculo,
        RegimeTrabalho regimeTrabalho,

        Boolean primeiroEmprego,

        LocalDate dataAdmissao,
        LocalDate dataInicioVinculo,
        LocalDate experienciaInicio,
        LocalDate experienciaFim
) {
}

