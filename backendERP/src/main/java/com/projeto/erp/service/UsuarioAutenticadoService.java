package com.projeto.erp.service;

import com.projeto.erp.exception.NegocioException;
import com.projeto.erp.modelo.Usuario;
import com.projeto.erp.repository.UsuarioRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAutenticadoService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioAutenticadoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public String getLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    public Usuario getUsuario() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NegocioException("Usuário não autenticado");
        }

        String login = authentication.getName();

        return usuarioRepository.findByLogin(login).orElseThrow(() -> new NegocioException("Usuário não encontrado"));
    }
}
