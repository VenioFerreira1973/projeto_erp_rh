package com.projeto.erp.modelo;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "colaborador_encargos")
public class ColaboradorEncargos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Colaborador colaborador;

    private String categoriaTrabalhador; // eSocial
    private String codigoFgts;
    private boolean opcaoFgts;
    private LocalDate dataOpcaoFgts;
    private BigDecimal aliquotaFgts;

    private String codigoInss;
    private boolean indicadorInss;
}

