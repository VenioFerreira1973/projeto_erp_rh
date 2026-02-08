package com.projeto.erp.dtos;

public record ColaboradorDocumentosRequestDTO(
        String cpf,
        String pisPasep,
        boolean possuiCtpsFisica,
        String ctpsNumero,
        String ctpsSerie,
        String registroEstrangeiro

) {
}
