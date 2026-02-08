package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.Status;
import com.projeto.erp.enumeracoes.StatusValidacao;

import java.time.LocalDate;

public record ColaboradorDocumentosResponseDTO(
        String cpf,
        String pisPasep,
        boolean possuiCtpsFisica,
        String ctpsNumero,
        String ctpsSerie,
        String registroEstrangeiro,
        Status status,
        String observacao,
        StatusValidacao statusValidacao

) {}
