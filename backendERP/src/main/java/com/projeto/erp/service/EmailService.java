package com.projeto.erp.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarResetSenha(String email, String link) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("venioferreira@gmail.com");
        message.setSubject("Redefinição de senha");
        message.setText("""
            Olá,

            Recebemos uma solicitação para redefinir sua senha.

            Clique no link abaixo:
            %s

            Se você não solicitou, ignore este e-mail.
            """.formatted(link));

        mailSender.send(message);
    }

    public void enviarSenhaNova(String email, String login, String senha) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("venioferreira@gmail.com");
        message.setSubject("Senha de acesso ao Humanix.");
        String texto = "Olá,\n\n" +
                "Agora você já tem seu usuário Humanix.\n\n" +
                "Acesse o Humanix:\n" +
                "http://localhost:5173/\n\n" +
                "Informe login - " + login + " e senha - " + senha + "\n\n" +
                "Para ter acesso ao sistema e iniciar seu Onboarding.";

        message.setText(texto);
        mailSender.send(message);
    }

    public void enviarAvisoPendencia(String email) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("venioferreira@gmail.com");
        message.setSubject("Pendência no processo de Onboarding.");
        String texto = "Olá,\n\n" +
                "Acesse o sistema Humanix.\n\n" +
                "Verifique seu cadastro pois há pendências a serem resolvidas.\n" +
                "http://localhost:5173/\n\n" ;

        message.setText(texto);
        mailSender.send(message);
    }

    public void enviarAvisoOnboardingFinalizado(String email) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("venioferreira@gmail.com");
        message.setSubject("Processo de Onboarding aprovado.");
        String texto = "Olá,\n\n" +
                "Seu processo de onboarding foi aprovado.\n\n" +
                "O RH da empresa entrará em contato em breve para orientá-lo para os próximos passos.\n" +
                "Obrigado.\n\n" ;

        message.setText(texto);
        mailSender.send(message);
    }
}
