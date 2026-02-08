package com.projeto.erp.dtos;

import java.util.Set;

public record UsuarioDTORequest(
        Long id,
        String login,
        String emailPessoal,
        String senha,
        boolean ativo,
        Set<PerfilDTO> perfis
) {}