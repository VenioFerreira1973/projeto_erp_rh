package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.RegimeTrabalho;
import com.projeto.erp.enumeracoes.TipoJornada;
import com.projeto.erp.modelo.ColaboradorSalario;

import java.util.List;

public record ColaboradorContratoUpdateDTO(

        TipoJornada tipoJornada,
        Integer horasSemanais,

        RegimeTrabalho regimeTrabalho,
        List<ColaboradorSalario> salarios

) {}
