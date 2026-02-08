package com.projeto.erp.modelo;

import com.projeto.erp.enumeracoes.*;
import jakarta.persistence.*;
import lombok.Setter;

import java.time.Instant;
import java.util.*;

@Setter
@Entity
@Table(name = "colaborador")
public class Colaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email_corporativo", nullable = false)
    private String emailCorporativo;

    @Column(name = "matricula", nullable = false, length = 20)
    private String matricula;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "onboarding_step", nullable = false)
    private OnboardingStep onboardingStep;

    @OneToMany(
            mappedBy = "colaborador",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ColaboradorDependentes> dependentes = new ArrayList<>();

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(name = "data_alteracao", nullable = false)
    private Instant dataAlteracao;

    public Colaborador() {}

    public void vincularUsuario(Usuario usuario) {
        this.usuario = Objects.requireNonNull(usuario);
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmailCorporativo() { return emailCorporativo; }
    public String getMatricula() { return matricula; }
    public Usuario getUsuario() { return usuario; }
    public Instant getDataCriacao() { return dataCriacao; }
    public Instant getDataAlteracao() { return dataAlteracao; }
    public OnboardingStep getOnboardingStep() { return onboardingStep; }

    @PrePersist
    protected void onCreate() {
        this.onboardingStep = OnboardingStep.DADOS_PESSOAIS;
        Instant now = Instant.now();
        this.dataCriacao = now;
        this.dataAlteracao = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.dataAlteracao = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Colaborador other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
