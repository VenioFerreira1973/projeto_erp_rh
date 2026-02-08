package com.projeto.erp.modelo;

import com.projeto.erp.dtos.ColaboradorDependentesRequestDTO;
import com.projeto.erp.enumeracoes.Status;
import com.projeto.erp.enumeracoes.TipoDependente;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "colaborador_dependentes")
public class ColaboradorDependentes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;

    @Enumerated(EnumType.STRING)
    @Column(name="tipo_dependente", nullable = false)
    private TipoDependente tipoDependente;

    @Column(name="nome", nullable = false)
    private String nome;

    @Column(name="cpf",length = 11)
    private String cpf;

    @Column(name="data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name="dependencia_ir", nullable = false)
    private boolean dependenciaIr;

    @Column(name="dependencia_salario_familia", nullable = false)
    private boolean dependenciaSalarioFamilia;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private Status status = Status.ATIVO;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(name = "data_alteracao", nullable = false)
    private Instant dataAlteracao;

    protected ColaboradorDependentes() {
    }

    private ColaboradorDependentes(
            Colaborador colaborador,
            TipoDependente tipoDependente,
            String nome,
            String cpf,
            LocalDate dataNascimento,
            boolean dependenciaIr,
            boolean dependenciaSalarioFamilia
    ) {
        this.colaborador = Objects.requireNonNull(colaborador);
        this.tipoDependente = Objects.requireNonNull(tipoDependente);
        this.nome = Objects.requireNonNull(nome);
        this.cpf = cpf;
        this.dataNascimento = Objects.requireNonNull(dataNascimento);
        this.dependenciaIr = dependenciaIr;
        this.dependenciaSalarioFamilia = dependenciaSalarioFamilia;
    }

    public static ColaboradorDependentes criar(
            Colaborador colaborador,
            ColaboradorDependentesRequestDTO request
    ) {
        return new ColaboradorDependentes(
                colaborador,
                request.tipoDependente(),
                request.nome(),
                request.cpf(),
                request.dataNascimento(),
                request.dependenciaIr(),
                request.dependenciaSalarioFamilia()
        );
    }


    public void atualizar(ColaboradorDependentesRequestDTO request) {
        this.tipoDependente = request.tipoDependente();
        this.nome = request.nome();
        this.cpf = request.cpf();
        this.dataNascimento = request.dataNascimento();
        this.dependenciaIr = request.dependenciaIr();
        this.dependenciaSalarioFamilia = request.dependenciaSalarioFamilia();
    }

    public void desativar() {
        this.status = Status.INATIVO;
    }

    public void ativar() {
        this.status = Status.ATIVO;
    }
    public boolean isDependenciaIr() {
        return dependenciaIr;
    }
    public boolean isDependenciaSalarioFamilia() {
        return dependenciaSalarioFamilia;
    }

    public Long getId() { return id; }
    public Colaborador getColaborador() {
        return colaborador;
    }
    public TipoDependente getTipoDependente() {
        return tipoDependente;
    }
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
    public String getCpf() {
        return cpf;
    }
    public String getNome() {
        return nome;
    }
    public Status getStatus() {
        return status;
    }
    public Instant getDataCriacao() {
        return dataCriacao;
    }
    public Instant getDataAlteracao() {
        return dataAlteracao;
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
        if (!(o instanceof ColaboradorDependentes other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

