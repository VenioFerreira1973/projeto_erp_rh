package com.projeto.erp.security;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String CARACTERES =
            "ABCDEFGHJKLMNPQRSTUVWXYZ" +
                    "abcdefghijkmnopqrstuvwxyz" +
                    "23456789";

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String gerarSenhaTemporaria(int tamanho) {
        StringBuilder senha = new StringBuilder(tamanho);

        for (int i = 0; i < tamanho; i++) {
            int index = RANDOM.nextInt(CARACTERES.length());
            senha.append(CARACTERES.charAt(index));
        }

        return senha.toString();
    }
}
