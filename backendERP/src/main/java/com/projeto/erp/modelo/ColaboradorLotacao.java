package com.projeto.erp.modelo;

import com.projeto.erp.enumeracoes.NivelCargo;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "colaborador_lotacao")
public class ColaboradorLotacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colaborador_id", nullable = false)
    private Colaborador colaborador;

    @ManyToOne
    private Cargo cargo;

    @ManyToOne
    private Departamento departamento;

    @ManyToOne
    private CentroCusto centroCusto;

    @ManyToOne
    private Colaborador gestor;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_cargo")
    private NivelCargo nivelCargo;

    @Column(name = "cargo_cbo")
    private String cargoCbo;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(name = "data_alteracao", nullable = false)
    private Instant dataAlteracao;

    protected ColaboradorLotacao() {}

    public ColaboradorLotacao(
            Colaborador colaborador,
            Cargo cargo,
            Departamento departamento,
            CentroCusto centroCusto,
            Colaborador gestor,
            LocalDate dataInicio
    ) {
        this.colaborador = Objects.requireNonNull(colaborador);
        this.cargo = Objects.requireNonNull(cargo);
        this.departamento = departamento;
        this.centroCusto = centroCusto;
        this.gestor = gestor;
        this.dataInicio = Objects.requireNonNull(dataInicio);

        this.dataCriacao = Instant.now();
        this.dataAlteracao = this.dataCriacao;
    }

    public Long getId() {
        return id;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public CentroCusto getCentroCusto() {
        return centroCusto;
    }

    public Colaborador getGestor() {
        return gestor;
    }

    public String getCargoCbo() {
        return cargoCbo;
    }

    public NivelCargo getNivelCargo() {
        return nivelCargo;
    }

    public Instant getDataCriacao() {
        return dataCriacao;
    }

    public Instant getDataAlteracao() {
        return dataAlteracao;
    }

    public void alterarCargo(Cargo novoCargo) {
        this.cargo = Objects.requireNonNull(novoCargo);
        this.dataAlteracao = Instant.now();
    }

    public void alterarDepartamento(Departamento departamento) {
        this.departamento = departamento;
        this.dataAlteracao = Instant.now();
    }

    public void altearGestor(Colaborador gestor) {
        this.gestor = gestor;
        this.dataAlteracao = Instant.now();
    }

    public void alterarColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
        this.dataAlteracao = Instant.now();
    }

}
