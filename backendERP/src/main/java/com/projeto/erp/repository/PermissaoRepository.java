package com.projeto.erp.repository;

import com.projeto.erp.modelo.Permissao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

    Optional<Permissao> getByDescricao(String roleAdmin);
}
