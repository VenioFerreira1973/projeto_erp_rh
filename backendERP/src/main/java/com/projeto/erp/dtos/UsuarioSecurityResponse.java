package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.UsuarioStatus;

import java.util.List;

public record UsuarioSecurityResponse(
        String login,
        UsuarioStatus status,
        List<PermissaoDTO> permissoes
) {}

