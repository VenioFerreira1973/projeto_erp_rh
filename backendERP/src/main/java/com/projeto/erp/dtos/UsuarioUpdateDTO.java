package com.projeto.erp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UsuarioUpdateDTO(
        String nome,
        String emailPessoal
) {}
