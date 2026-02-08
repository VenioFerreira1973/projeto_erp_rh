package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.RegimeTrabalho;
import com.projeto.erp.enumeracoes.TipoContrato;
import com.projeto.erp.enumeracoes.TipoJornada;
import com.projeto.erp.enumeracoes.TipoVinculo;

import java.time.LocalDate;

public record ColaboradorContratoCreateDTO(

        Long colaboradorId,
        Long empresaId,

        TipoContrato tipoContrato,
        TipoJornada tipoJornada,
        Integer horasSemanais,

        TipoVinculo tipoVinculo,
        RegimeTrabalho regimeTrabalho,

        boolean primeiroEmprego,

        LocalDate dataAdmissao

) {}
