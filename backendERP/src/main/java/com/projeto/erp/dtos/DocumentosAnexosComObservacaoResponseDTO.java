package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.StatusValidacao;

import java.util.List;

public record DocumentosAnexosComObservacaoResponseDTO(
        String observacao,
        StatusValidacao statusValidacao,
        List<ColaboradorDocumentosAnexosResponseDTO> documentos
) {
}