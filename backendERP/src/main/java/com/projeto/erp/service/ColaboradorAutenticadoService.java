package com.projeto.erp.service;

import com.projeto.erp.modelo.Colaborador;
import com.projeto.erp.exception.NegocioException;
import com.projeto.erp.repository.ColaboradorRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ColaboradorAutenticadoService {

    private final ColaboradorRepository colaboradorRepository;

    public ColaboradorAutenticadoService(ColaboradorRepository colaboradorRepository) {
        this.colaboradorRepository = colaboradorRepository;
    }

    public Colaborador getColaborador() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NegocioException("Usuário não autenticado");
        }

        String login = authentication.getName();

        return colaboradorRepository.findByUsuario_Login(login)
                .orElseThrow(() ->
                        new NegocioException("Colaborador não encontrado para o usuário logado")
                );
    }
}
