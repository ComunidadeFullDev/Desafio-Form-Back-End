package com.fulldev.formulario.form.service;

import com.fulldev.formulario.security.domain.model.entity.User;
import com.fulldev.formulario.security.domain.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Service
@EnableAsync
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Async("emailTaskExecutor")
    @Retryable(
            value = { MailException.class, jakarta.mail.MessagingException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void sendVerificationEmail(String to, String subject, String verificationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            User user = (User) userRepository.findByEmail(to);
            if (user != null && user.isVerified()) {
                helper.setText(buildEmailContent(), true);
            } else {
                helper.setText(buildVerificationEmailContent(verificationLink), true);
            }

            mailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("emailTaskExecutor")
    @Retryable(
            value = { MailException.class, jakarta.mail.MessagingException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            mailSender.send(message);
            System.out.println("Email enviado com sucesso para " + to);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Async("emailTaskExecutor")
    @Retryable(
            value = { MailException.class, jakarta.mail.MessagingException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000)
    )
    public void sendPasswordResetEmail(String to, String subject, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(buildPasswordResetEmailContent(resetLink), true);

            mailSender.send(message);
            System.out.println("email de redefinição enviado");
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildPasswordResetEmailContent(String resetLink) {
        return "<p>Olá,</p>" +
                "<p>Recebemos uma solicitação para redefinir sua senha. Clique no link abaixo para redefini-la:</p>" +
                "<a href=\"" + resetLink + "\">Redefinir Senha</a>" +
                "<p>Se você não solicitou a redefinição, ignore este e-mail.</p>";
    }

    private String buildVerificationEmailContent(String verificationLink) {
        return "<p>Olá,</p>" +
                "<p>Obrigado por se cadastrar. Clique no link abaixo para verificar sua conta:</p>" +
                "<a href=\"" + verificationLink + "\">Verificar Conta</a>" +
                "<p>Se você não se cadastrou, ignore este e-mail.</p>";
    }

    private String buildEmailContent() {
        return "<p>Olá,</p>" +
                "<p>Estamos muito felizes em informar que seu endereço de e-mail foi verificado com sucesso.</p>" +
                "<p>Agora você pode aproveitar todos os recursos disponíveis no contrutor de formulários da fulldev.</p>" +
                "<p>Mais uma vez, obrigado por fazer parte da nossa comunidade!</p>" +
                "<p>Atenciosamente,</p>" +
                "<p>Equipe da fullDev</p>";
    }
}