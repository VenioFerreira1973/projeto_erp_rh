package com.projeto.erp.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "plano_odontologico")
public class PlanoOdontologico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String operadora;

    @Column(nullable = false)
    private BigDecimal valorMensal;

    private BigDecimal coparticipacao;

    private boolean cobreOrtodontia;
    private boolean cobreImplante;
    private boolean incluiDependentes;

    private LocalDate dataInicioVigencia;
    private LocalDate dataFimVigencia;

    private boolean ativo;

    // getters e setters
}
