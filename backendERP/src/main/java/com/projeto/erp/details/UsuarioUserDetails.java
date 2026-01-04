package com.projeto.erp.details;

import com.projeto.erp.dtos.PermissaoDTO;
import com.projeto.erp.dtos.UsuarioSecurityDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UsuarioUserDetails implements UserDetails {

    private final UsuarioSecurityDTO usuario;

    public UsuarioUserDetails(UsuarioSecurityDTO usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuario.permissoes().stream()
                .map(PermissaoDTO::descricao)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return usuario.login();
    }

    @Override
    public String getPassword() {
        return usuario.senha();
    }

    @Override
    public boolean isEnabled() {
        return usuario.ativo();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
