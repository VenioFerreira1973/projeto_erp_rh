package com.projeto.erp.repository;

import com.projeto.erp.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByLogin(String login);

    @Query("""
                SELECT u FROM Usuario u
                JOIN FETCH u.perfis pf
                JOIN FETCH pf.permissoes
                WHERE u.login = :login
            """)
    Optional<Usuario> findByLoginWithPerfisAndPermissoes(@Param("login") String login);

}
