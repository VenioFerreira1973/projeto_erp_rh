package com.projeto.erp.modelo;

import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;

@Entity
@Table(name = "perfil")
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String descricao;

    @Column(nullable = false)
    private boolean ativo = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "perfil_permissao",
            joinColumns = @JoinColumn(name = "perfil_id"),
            inverseJoinColumns = @JoinColumn(name = "permissao_id")
    )
    private Set<Permissao> permissoes = new HashSet<>();

    public Perfil(){}

    public Perfil(String descricao) {
        this.descricao = descricao;
        this.ativo = true;
    }

    public void adicionarPermissao(Permissao p) { this.permissoes.add(p); }
    public void removerPermissao(Permissao p) { this.permissoes.remove(p); }

    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public boolean isAtivo() { return ativo; }
    public Set<Permissao> getPermissoes() { return permissoes; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Perfil other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}