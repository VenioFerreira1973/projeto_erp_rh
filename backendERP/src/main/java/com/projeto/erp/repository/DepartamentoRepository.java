package com.projeto.erp.repository;

import com.projeto.erp.modelo.Departamento;
import com.projeto.erp.modelo.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {

}
