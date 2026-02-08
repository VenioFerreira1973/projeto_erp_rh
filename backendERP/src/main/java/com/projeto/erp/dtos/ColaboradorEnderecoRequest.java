package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.TipoEndereco;

import java.time.Instant;

public record ColaboradorEnderecoRequest(
        TipoEndereco tipoEndereco,
        String cep,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String municipio,
        String uf,
        String pais
) {}
