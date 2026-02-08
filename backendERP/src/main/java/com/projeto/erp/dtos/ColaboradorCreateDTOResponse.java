package com.projeto.erp.dtos;

import java.time.Instant;

public record ColaboradorCreateDTOResponse(
        Long id,
        String nome,
        String emailCorporativo,
        UsuarioDTOResponse usuario,
        String matricula,
        String usuarioLogin,
        String senhaTemporaria,
        Instant dataCriacao,
        Instant dataAlteracao

) {}
