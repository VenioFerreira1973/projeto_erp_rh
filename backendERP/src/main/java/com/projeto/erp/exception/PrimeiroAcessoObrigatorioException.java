package com.projeto.erp.exception;

public class PrimeiroAcessoObrigatorioException extends RuntimeException {
    public PrimeiroAcessoObrigatorioException() {
        super("Usuário precisa concluir o primeiro acesso");
    }
}
