package com.projeto.erp.dtos;

import java.util.List;

public record UsuarioSecurityResponse(
        String login,
        boolean ativo,
        List<PermissaoDTO> permissoes
) {}

