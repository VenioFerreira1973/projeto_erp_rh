package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.FormaPagamento;
import com.projeto.erp.enumeracoes.Status;
import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.enumeracoes.TipoConta;

public record ColaboradorDadosBancariosResponseDTO(
        String bancoCodigo,
        String bancoNome,
        String agencia,
        String conta,
        String digitoConta,
        TipoConta tipoConta,
        String chavePix,
        FormaPagamento formaPagamento,
        Status status,
        String observacao,
        StatusValidacao statusValidacao

) {
}
