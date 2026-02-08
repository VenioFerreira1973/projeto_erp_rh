package com.projeto.erp.dtos;

import java.util.Set;

public record UsuarioDTOResponse(
        Long id,
        String login,
        String emailPessoal,
        boolean ativo,
        Set<PerfilDTO> perfis
) {}