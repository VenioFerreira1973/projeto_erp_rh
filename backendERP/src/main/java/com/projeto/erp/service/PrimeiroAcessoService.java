package com.projeto.erp.service;

import com.projeto.erp.dtos.LoginResponse;
import com.projeto.erp.dtos.UsuarioSecurityDTO;
import com.projeto.erp.dtos.UsuarioSecurityResponse;
import com.projeto.erp.mapper.UsuarioSecurityMapper;
import com.projeto.erp.modelo.Usuario;
import com.projeto.erp.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrimeiroAcessoService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsuarioSecurityMapper usuarioSecurityMapper;

    public PrimeiroAcessoService(
            UsuarioRepository usuarioRepository,
            BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService, UsuarioSecurityMapper usuarioSecurityMapper
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.usuarioSecurityMapper = usuarioSecurityMapper;
    }

    @Transactional
    public LoginResponse definirNovaSenha(String login, String novaSenha) {

        Usuario usuario = usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (!usuario.isPrimeiroAcesso()) {
            throw new IllegalStateException("O primeiro acesso já foi finalizado");
        }

        if (novaSenha == null || novaSenha.isBlank()) {
            throw new IllegalArgumentException("A senha não pode ser vazia");
        }

        if (novaSenha.length() < 8) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres");
        }

        if (passwordEncoder.matches(novaSenha, usuario.getSenha())) {
            throw new IllegalArgumentException("A nova senha deve ser diferente da senha atual");
        }

        usuario.alterarSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);


        UsuarioSecurityDTO dto = usuarioSecurityMapper.toSecurityDTO(usuario);

        String token = jwtService.gerarToken(dto);

        UsuarioSecurityResponse usuarioResponse =
                usuarioSecurityMapper.toSecurityResponse(usuario);

        return new LoginResponse(
                token,
                usuarioResponse,
                usuario.isPrimeiroAcesso()
        );
    }
}
