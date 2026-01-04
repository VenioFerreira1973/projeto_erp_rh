package com.projeto.erp.service;

import com.projeto.erp.modelo.Perfil;
import com.projeto.erp.modelo.Permissao;
import com.projeto.erp.modelo.Usuario;
import com.projeto.erp.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Set;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioUserDetailsService service;

    @Test
    void deveCarregarUsuarioComPermissoes() {

        Permissao perm = new Permissao("ROLE_ADMIN");

        Perfil perfil = new Perfil("ADMIN");
        perfil.adicionarPermissao(perm);

        Usuario usuario = new Usuario("admin", "admin@empresa.com", "senha-criptografada");
        usuario.adicionarPerfil(perfil);

        when(usuarioRepository.findByLoginWithPerfisAndPermissoes("admin"))
                .thenReturn(Optional.of(usuario));

        UserDetails userDetails = service.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        assertEquals("senha-criptografada", userDetails.getPassword());
        assertTrue(
                userDetails.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
        );
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {

        when(usuarioRepository.findByLoginWithPerfisAndPermissoes("inexistente"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername("inexistente")
        );
    }
}
