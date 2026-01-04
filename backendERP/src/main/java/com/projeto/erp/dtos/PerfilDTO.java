package com.projeto.erp.dtos;

import java.util.Set;

public record PerfilDTO(
        Long id,
        String descricao,
        boolean ativo,
        Set<PermissaoDTO> permissoes

) {}
