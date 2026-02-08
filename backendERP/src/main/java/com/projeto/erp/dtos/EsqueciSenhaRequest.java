package com.projeto.erp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EsqueciSenhaRequest {

    @NotBlank
    private String login;
}
