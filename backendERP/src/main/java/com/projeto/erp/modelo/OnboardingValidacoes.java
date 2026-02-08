package com.projeto.erp.modelo;

import com.projeto.erp.dtos.OnboardingValidacoesRequestDTO;
import com.projeto.erp.enumeracoes.OnboardingValidacaoStep;
import com.projeto.erp.enumeracoes.StatusValidacao;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(
        name = "onboarding_validacoes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"colaborador_id", "onboarding_validacao_step"})
        }
)
public class OnboardingValidacoes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="colaborador_id", nullable=false)
    private Long colaboradorId;

    @Enumerated(EnumType.STRING)
    @Column(name = "onboarding_validacao_step", nullable = false)
    private OnboardingValidacaoStep onboardingValidacaoStep;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_validacao", length = 15, nullable = false)
    private StatusValidacao statusValidacao = StatusValidacao.PENDENTE;


    @Column(name="observacao")
    private String observacao;

    @Column(name="validado_por")
    private Long validadoPor;

    @Column(name="validado_em")
    private Instant validadoEm;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(name = "data_alteracao", nullable = false)
    private Instant dataAlteracao;

    public  OnboardingValidacoes(){}

    private OnboardingValidacoes(Long colaboradorId, OnboardingValidacaoStep onboardingValidacaoStep,
                                 StatusValidacao status, String observacao) {
        this.colaboradorId = colaboradorId;
        this.onboardingValidacaoStep = onboardingValidacaoStep;
        this.statusValidacao = status;
        this.observacao = observacao;
    }

    public static OnboardingValidacoes criar(
            OnboardingValidacoes validacoes,
            OnboardingValidacoesRequestDTO request
    ) {
        return new OnboardingValidacoes(
                request.colaboradorId(),
                request.onboardingValidacaoStep(),
                request.statusValidacao(),
                request.observacao()
        );
    }

    public void atualizar(OnboardingValidacoesRequestDTO request) {
        this.observacao = request.observacao();
        this.statusValidacao = request.statusValidacao();
        this.onboardingValidacaoStep = request.onboardingValidacaoStep();
        this.colaboradorId = request.colaboradorId();
    }

    public void atualizarObservacao(String observacao) {
        this.observacao = observacao;
    }

    public static OnboardingValidacoes criar(
            Long colaboradorId,
            OnboardingValidacaoStep onboardingValidacaoStep,
            StatusValidacao status,
            String observacao

    ) {
        if (colaboradorId == null || onboardingValidacaoStep == null) {
            throw new IllegalArgumentException("Colaborador e step são obrigatórios");
        }
        return new OnboardingValidacoes(colaboradorId, onboardingValidacaoStep, status, observacao);
    }

    public StatusValidacao getStatusValidacao() {
        return statusValidacao;
    }
    public Long getColaboradorId(){
        return  colaboradorId;
    }

    public OnboardingValidacaoStep getOnboardingValidacaoStep(){
        return onboardingValidacaoStep;
    }

    public String getObservacao(){
        return observacao;
    }

    public void alterarStatus(StatusValidacao statusValidacao) {
        this.statusValidacao = statusValidacao;
    }

    public void aprovar(Long rhId) {
        this.statusValidacao = StatusValidacao.APROVADO;
        this.validadoPor = rhId;
        this.validadoEm = Instant.now();
    }

    public void reprovar(String observacao, Long rhId) {
        this.statusValidacao = StatusValidacao.REPROVADO;
        this.observacao = observacao;
        this.validadoPor = rhId;
        this.validadoEm = Instant.now();
    }

    @PrePersist
    protected void onCreate() {
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
        if (!(o instanceof OnboardingValidacoes other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
