package com.projeto.erp.dtos;

import java.util.List;

public record UsuarioSecurityDTO(
        String login,
        String senha,
        boolean ativo,
        List<PermissaoDTO> permissoes
) {}
