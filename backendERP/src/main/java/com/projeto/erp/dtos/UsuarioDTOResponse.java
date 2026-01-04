package com.projeto.erp.dtos;

import java.util.Set;

public record UsuarioDTOResponse(
        Long id,
        String login,
        String email,
        boolean ativo,
        Set<PerfilDTO> perfis
) {}