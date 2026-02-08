package com.projeto.erp.repository;

import com.projeto.erp.enumeracoes.OnboardingStep;
import com.projeto.erp.modelo.Colaborador;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {

    boolean existsByMatricula(String matricula);

    Optional<Colaborador> findByUsuario_Login(String login);

    @Query("""
    select distinct f
    from Colaborador f
    left join fetch f.usuario
""")
    List<Colaborador> findAllComRelacionamentos();


    @Query("""
    select max(f.matricula)
    from Colaborador f
""")
    String findUltimaMatricula();

    List<Colaborador> findByOnboardingStepNot(OnboardingStep onboardingStep);

    List<Colaborador> findByOnboardingStep(OnboardingStep onboardingStep);

    Optional<Colaborador> findByUsuarioId(Long id);
}
