package com.projeto.erp.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "colaborador_beneficio")
public class ColaboradorBeneficio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Colaborador colaborador;

    @ManyToOne(fetch = FetchType.LAZY)
    private PlanoSaude planoSaude;

    @ManyToOne(fetch = FetchType.LAZY)
    private PlanoOdontologico planoOdontologico;

    private boolean valeTransporte;
    private BigDecimal valeRefeicao;
    private BigDecimal valeAlimentacao;
    private BigDecimal auxilioCreche;

    private String outrosBeneficios;
}

