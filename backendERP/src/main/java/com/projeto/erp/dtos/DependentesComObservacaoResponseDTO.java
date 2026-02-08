package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.StatusValidacao;

import java.util.List;

public record DependentesComObservacaoResponseDTO(
        String observacao,
        StatusValidacao statusValidacao,
        List<ColaboradorDependentesResponseDTO> dependentes
) {
}