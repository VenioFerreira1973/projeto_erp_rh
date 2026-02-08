package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.ContratoStatus;

import java.time.LocalDate;

public record ColaboradorContratoListDTO(

        Long id,
        String matricula,
        String colaboradorNome,
        ContratoStatus contratoStatus,
        LocalDate dataAdmissao

) {}

