package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.*;

import java.time.Instant;
import java.time.LocalDate;

public record ColaboradorContratoDTOResponse(
        Long id,

        Long colaboradorId,
        String colaboradorNome,
        String matricula,

        Long empresaId,
        String empresaNome,

        String sindicato,

        TipoContrato tipoContrato,
        PrazoContrato prazoContrato,
        TipoJornada tipoJornada,
        Integer horasSemanais,

        TipoVinculo tipoVinculo,
        RegimeTrabalho regimeTrabalho,

        ContratoStatus contratoStatus,

        boolean primeiroEmprego,

        LocalDate dataAdmissao,
        LocalDate dataInicioVinculo,
        LocalDate dataDemissao,
        LocalDate dataFimVinculo,

        MotivoDemissao motivoDemissao,

        Instant dataCriacao,
        Instant dataAlteracao
) {}
