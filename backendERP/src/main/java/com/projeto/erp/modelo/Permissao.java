package com.projeto.erp.modelo;

import com.projeto.erp.enumeracoes.Status;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status;

    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public Instant getDataCriacao() { return dataCriacao; }
    public Instant getDataAlteracao() { return dataAlteracao; }

    public Permissao(String descricao){
        this.descricao = descricao;
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
        if (!(o instanceof Permissao other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }


    public void ativar(Permissao permissao) {
        this.status = Status.ATIVO;
    }

    public void inativar(Permissao permissao) {
        this.status = Status.INATIVO;
    }
}