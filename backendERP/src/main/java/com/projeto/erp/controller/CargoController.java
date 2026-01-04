package com.projeto.erp.controller;

import com.projeto.erp.dtos.CargoDTO;
import com.projeto.erp.service.CargoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cargos")
public class CargoController {

    private final CargoService cargoService;

    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @PreAuthorize("hasAuthority('PERFIL_ADMIN')")
    @GetMapping
    public List<CargoDTO> listar() {
        return cargoService.listar();
    }
}
