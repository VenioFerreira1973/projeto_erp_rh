package com.projeto.erp.service;

import com.projeto.erp.modelo.PasswordResetToken;
import com.projeto.erp.modelo.Usuario;
import com.projeto.erp.repository.PasswordResetTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetTokenService {

    private PasswordResetTokenRepository repository;

    public PasswordResetTokenService(PasswordResetTokenRepository repository) {
        this.repository = repository;
    }

    public PasswordResetToken criar(Usuario usuario) {

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUsuario(usuario);
        token.setExpiraEm(LocalDateTime.now().plusMinutes(15));
        token.setUsado(false);

        return repository.save(token);
    }

    public PasswordResetToken validar(String token) {
        PasswordResetToken prt = repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (prt.isUsado() || prt.getExpiraEm().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado ou já utilizado");
        }

        return prt;
    }
}
