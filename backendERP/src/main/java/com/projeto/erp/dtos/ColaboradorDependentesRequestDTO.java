package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.TipoDependente;

import java.time.LocalDate;

public record ColaboradorDependentesRequestDTO(
        Long id,
        TipoDependente tipoDependente,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        boolean dependenciaIr,
        boolean dependenciaSalarioFamilia
) {
}