package com.projeto.erp.dtos;

import java.time.Instant;

public record ColaboradorDTOResponse(
        Long id,
        String nome,
        String matricula,
        UsuarioDTOResponse usuario,
        String usuarioLogin,
        Instant dataCriacao,
        Instant dataAlteracao

) {}
