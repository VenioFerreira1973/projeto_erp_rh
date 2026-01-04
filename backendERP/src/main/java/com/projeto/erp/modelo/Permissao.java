package com.projeto.erp.modelo;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "permissao")
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String descricao;

    @Column(nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(nullable = false)
    private Instant dataAlteracao;

    public Permissao() {
    }

    public Permissao(String descricao) {

        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public Instant getDataCriacao() { return dataCriacao; }
    public Instant getDataAlteracao() { return dataAlteracao; }

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
        if (!(o instanceof Permissao other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    public void setId(Long id) { this.id = id; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}