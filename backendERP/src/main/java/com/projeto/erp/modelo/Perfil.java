package com.projeto.erp.modelo;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import com.projeto.erp.enumeracoes.Status;
import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "perfil")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String descricao;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "perfil_permissao",
            joinColumns = @JoinColumn(name = "perfil_id"),
            inverseJoinColumns = @JoinColumn(name = "permissao_id")
    )
    private Set<Permissao> permissoes = new HashSet<>();

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;

    @Column(name = "data_alteracao", nullable = false)
    private Instant dataAlteracao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status;

    public Perfil(String descricao){
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

    public void adicionarPermissao(Permissao p) { this.permissoes.add(p); }
    public void removerPermissao(Permissao p) { this.permissoes.remove(p); }

    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public Set<Permissao> getPermissoes() { return permissoes; }
    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Perfil other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    public void ativar(Perfil perfil) {
        this.status = Status.ATIVO;
    }

    public void inativar(Perfil perfil) {
        this.status = Status.INATIVO;
    }
}