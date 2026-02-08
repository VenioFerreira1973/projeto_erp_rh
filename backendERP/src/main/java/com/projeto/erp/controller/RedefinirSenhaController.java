package com.projeto.erp.controller;

import com.projeto.erp.dtos.EsqueciSenhaRequest;
import com.projeto.erp.dtos.ResetarSenhaRequest;
import com.projeto.erp.modelo.PasswordResetToken;
import com.projeto.erp.modelo.Usuario;
import com.projeto.erp.repository.PasswordResetTokenRepository;
import com.projeto.erp.repository.UsuarioRepository;
import com.projeto.erp.service.EmailService;
import com.projeto.erp.service.PasswordResetTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class RedefinirSenhaController {

    private final UsuarioRepository usuarioRepository;

    private final PasswordResetTokenService tokenService;

    private final PasswordResetTokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    @Value("${app.front-url}")
    private String frontUrl;

    public RedefinirSenhaController(UsuarioRepository usuarioRepository, PasswordResetTokenService tokenService, PasswordResetTokenRepository tokenRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.tokenService = tokenService;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @GetMapping("/password/validate")
    public ResponseEntity<?> validarToken(@RequestParam String token) {
        tokenService.validar(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<?> esqueciSenha(@Valid @RequestBody EsqueciSenhaRequest dto) {

        usuarioRepository.findByLogin(dto.getLogin())
                .ifPresent(usuario -> {
                    PasswordResetToken token = tokenService.criar(usuario);
                    String link = frontUrl + "/resetar-senha?token=" + token.getToken();
                    emailService.enviarResetSenha(usuario.getEmailPessoal(), link);
                });


        return ResponseEntity.ok(
                Map.of("message", "Se o usuário existir, enviamos um e-mail")
        );
    }

    @PostMapping("/password/reset")
    public ResponseEntity<?> resetarSenha(@Valid @RequestBody ResetarSenhaRequest dto) {

        PasswordResetToken token = tokenService.validar(dto.getToken());

        Usuario usuario = token.getUsuario();
        usuario.alterarSenha(passwordEncoder.encode(dto.getNovaSenha()));
        usuario.desmarcarPrimeiroAcesso();

        usuarioRepository.save(usuario);

        token.setUsado(true);
        tokenRepository.save(token);

        return ResponseEntity.ok().build();
    }


}
