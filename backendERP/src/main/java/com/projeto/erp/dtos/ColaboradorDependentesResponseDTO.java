package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.TipoDependente;

import java.time.LocalDate;

public record ColaboradorDependentesResponseDTO(
        Long id,
        TipoDependente tipoDependente,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        boolean dependenciaIr,
        boolean dependenciaSalarioFamilia

) {
}
