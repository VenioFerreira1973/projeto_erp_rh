package com.projeto.erp.controller;

import com.projeto.erp.dtos.*;
import com.projeto.erp.modelo.Usuario;
import com.projeto.erp.repository.UsuarioRepository;
import com.projeto.erp.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginDTO) {

        Usuario usuario = usuarioRepository.findByLoginWithPerfisAndPermissoes(loginDTO.login())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(loginDTO.senha(), usuario.getSenha())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        List<PermissaoDTO> permissoes = usuario.getPerfis().stream()
                .flatMap(p -> p.getPermissoes().stream())
                .map(per -> new PermissaoDTO(per.getId(), per.getDescricao()))
                .collect(Collectors.toList());

        UsuarioSecurityDTO dto = new UsuarioSecurityDTO(
                usuario.getLogin(),
                usuario.getSenha(),
                usuario.isAtivo(),
                permissoes
        );

        UsuarioSecurityResponse usuarioSecurityResponse = new UsuarioSecurityResponse(
                usuario.getLogin(),
                usuario.isAtivo(),
                permissoes
        );

        String token = jwtService.gerarToken(dto);

        LoginResponse loginResponse = new LoginResponse(
                token,
                usuarioSecurityResponse
        );

        return ResponseEntity.ok(loginResponse);
    }

}



