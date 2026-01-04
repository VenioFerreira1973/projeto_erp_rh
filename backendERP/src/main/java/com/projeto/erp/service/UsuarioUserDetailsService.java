package com.projeto.erp.service;

import com.projeto.erp.details.UsuarioUserDetails;
import com.projeto.erp.dtos.PermissaoDTO;
import com.projeto.erp.dtos.UsuarioSecurityDTO;
import com.projeto.erp.modelo.Usuario;
import com.projeto.erp.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByLoginWithPerfisAndPermissoes(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));


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

        return new UsuarioUserDetails(dto);
    }

}
