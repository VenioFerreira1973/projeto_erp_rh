package com.projeto.erp.modelo;

import com.projeto.erp.enumeracoes.Status;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "funcionario")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    @Column(name = "data_demissao")
    private LocalDate dataDemissao;

    @Column(name = "salario", nullable = false, precision = 12, scale = 2)
    private BigDecimal salario;

    @Column(name = "gerente", nullable = false)
    private boolean gerente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id")
    private Cargo cargo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id")
    private Departamento departamento;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(name = "data_alteracao", nullable = false)
    private Instant dataAlteracao;

    protected Funcionario() {}

    public Funcionario(
            String nome,
            BigDecimal salario,
            LocalDate dataAdmissao,
            Status status,
            boolean gerente
    ) {
        this.nome = Objects.requireNonNull(nome);
        this.salario = Objects.requireNonNull(salario);
        this.dataAdmissao = Objects.requireNonNull(dataAdmissao);
        this.status = Objects.requireNonNull(status);
        this.gerente = gerente;
    }

    public void alterarSalario(BigDecimal novoSalario) {
        this.salario = Objects.requireNonNull(novoSalario);
    }

    public void promoverParaGerente() {
        this.gerente = true;
    }

    public void removerGerencia() {
        this.gerente = false;
    }

    public void desligar(LocalDate dataDemissao) {
        this.status = Status.INATIVO;
        this.dataDemissao = Objects.requireNonNull(dataDemissao);
    }

    public void ligar(LocalDate dataDemissao) {
        this.status = Status.ATIVO;
        this.dataDemissao = null;
    }

    public void alterarCargo(Cargo cargo) {
        this.cargo = Objects.requireNonNull(cargo);
    }

    public void alterarDepartamento(Departamento departamento) {
        this.departamento = Objects.requireNonNull(departamento);
    }

    public void vincularUsuario(Usuario usuario) {
        this.usuario = Objects.requireNonNull(usuario);
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public Status getStatus() { return status; }
    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public LocalDate getDataDemissao() { return dataDemissao; }
    public BigDecimal getSalario() { return salario; }
    public boolean isGerente() { return gerente; }
    public Cargo getCargo() { return cargo; }
    public Departamento getDepartamento() { return departamento; }
    public Usuario getUsuario() { return usuario; }

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
        if (!(o instanceof Funcionario other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
