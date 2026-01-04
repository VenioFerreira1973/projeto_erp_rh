package com.projeto.erp.repository;

import com.projeto.erp.modelo.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    List<Funcionario> findByDepartamentoId(Long departamentoId);
    List<Funcionario> findByCargoId(Long cargoId);
    List<Funcionario> findByGerenteTrue();
}
