package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.CorRaca;
import com.projeto.erp.enumeracoes.EstadoCivil;
import com.projeto.erp.enumeracoes.Genero;

import java.time.LocalDate;

public record ColaboradorDadosPessoaisRequest(
        LocalDate dataNascimento,
        EstadoCivil estadoCivil,
        Genero genero,
        CorRaca corRaca,
        String nacionalidade
) {}
