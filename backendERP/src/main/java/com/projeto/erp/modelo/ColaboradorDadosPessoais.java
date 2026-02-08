package com.projeto.erp.modelo;

import com.projeto.erp.dtos.ColaboradorDadosPessoaisRequest;
import com.projeto.erp.enumeracoes.CorRaca;
import com.projeto.erp.enumeracoes.EstadoCivil;
import com.projeto.erp.enumeracoes.Genero;
import com.projeto.erp.enumeracoes.Status;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "colaborador_dados_pessoais")
public class ColaboradorDadosPessoais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id", nullable = false, unique = true)
    private Colaborador colaborador;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_civil")
    private EstadoCivil estadoCivil;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero")
    private Genero genero;

    @Enumerated(EnumType.STRING)
    @Column(name = "cor_raca")
    private CorRaca corRaca;

    @Column(name = "nacionalidade", nullable = false)
    private String nacionalidade;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(name = "data_alteracao", nullable = false)
    private Instant dataAlteracao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 10, nullable = false)
    private Status status = Status.ATIVO;

    protected ColaboradorDadosPessoais() {}

    private ColaboradorDadosPessoais(
            Colaborador colaborador,
            LocalDate dataNascimento,
            EstadoCivil estadoCivil,
            Genero genero,
            CorRaca corRaca,
            String nacionalidade
    ) {
        this.colaborador = Objects.requireNonNull(colaborador);
        this.dataNascimento = Objects.requireNonNull(dataNascimento);
        this.nacionalidade = Objects.requireNonNull(nacionalidade);
        this.estadoCivil = estadoCivil;
        this.genero = genero;
        this.corRaca = corRaca;
    }

    public static ColaboradorDadosPessoais criar(
            Colaborador colaborador,
            ColaboradorDadosPessoaisRequest request
    ) {
        ColaboradorDadosPessoais entity = new ColaboradorDadosPessoais(
                colaborador,
                request.dataNascimento(),
                request.estadoCivil(),
                request.genero(),
                request.corRaca(),
                request.nacionalidade()
        );
        return entity;
    }

    public void atualizar(ColaboradorDadosPessoaisRequest request) {
        this.dataNascimento = request.dataNascimento();
        this.estadoCivil = request.estadoCivil();
        this.genero = request.genero();
        this.corRaca = request.corRaca();
        this.nacionalidade = request.nacionalidade();
    }

    public boolean isEstrangeiro() {
        return !"Brasil".equalsIgnoreCase(nacionalidade);
    }

    public Long getId() { return id; }
    public Colaborador getColaborador() {
        return colaborador;
    }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public EstadoCivil getEstadoCivil() { return estadoCivil; }
    public Genero getGenero() { return genero; }
    public CorRaca getCorRaca() { return corRaca; }
    public String getNacionalidade() { return nacionalidade; }
    public Status getStatus() { return status; }
    public Instant getDataCriacao() { return dataCriacao; }
    public Instant getDataAlteracao() { return dataAlteracao; }


    public void ativar() {
        this.status = Status.ATIVO;
    }

    public void desativar() {
        this.status = Status.INATIVO;
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
        if (!(o instanceof ColaboradorDadosPessoais other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
