package com.projeto.erp.dtos;

import com.projeto.erp.enumeracoes.UsuarioStatus;

import java.util.List;

public record UsuarioSecurityDTO(
        String login,
        String senha,
        UsuarioStatus status,
        List<PermissaoDTO> permissoes,
        boolean primeiroAcesso
) {}
