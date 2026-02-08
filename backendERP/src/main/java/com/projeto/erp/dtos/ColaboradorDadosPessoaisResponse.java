package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.*;

import java.time.LocalDate;

public record ColaboradorDadosPessoaisResponse(
        LocalDate dataNascimento,
        EstadoCivil estadoCivil,
        Genero genero,
        CorRaca corRaca,
        String nacionalidade,
        Status status,
        String observacao,
        StatusValidacao statusValidacao

) {}
