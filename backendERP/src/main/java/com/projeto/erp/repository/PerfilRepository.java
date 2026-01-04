package com.projeto.erp.repository;

import com.projeto.erp.modelo.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Long> {

    Optional<Perfil> getByDescricao(String admin);
}
