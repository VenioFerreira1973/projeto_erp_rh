package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.StatusValidacao;
import com.projeto.erp.enumeracoes.TipoEndereco;

import java.time.Instant;

public record ColaboradorEnderecoResponse(
        TipoEndereco tipoEndereco,
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String municipio,
        String uf,
        String pais,
        String observacao,
        StatusValidacao statusValidacao
) {}
