package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.FormaPagamento;
import com.projeto.erp.enumeracoes.TipoConta;

public record ColaboradorDadosBancariosRequestDTO(
        String bancoCodigo,
        String bancoNome,
        String agencia,
        String conta,
        String digitoConta,
        TipoConta tipoConta,
        String chavePix,
        FormaPagamento formaPagamento

) {}

