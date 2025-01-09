package com.fulldev.formulario;

import com.fulldev.formulario.security.domain.model.entity.User;
import com.fulldev.formulario.security.domain.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import com.fulldev.formulario.security.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;


class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EmailService emailService;

    public EmailServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendVerificationEmail_Success() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        String to = "test@example.com";
        String subject = "Confirmação de Cadastro";
        String verificationLink = "http://localhost:3000/verify?token=123";

        User mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn(to);
        when(userRepository.findByEmail(to)).thenReturn(mockUser);

        CompletableFuture.runAsync(() -> emailService.sendVerificationEmail(to, subject, verificationLink))
                .join();

        verify(mailSender, times(1)).send(mimeMessage);
    }




    @Test
    void testSendSimpleEmail_Success() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        String to = "simple@example.com";
        String subject = "Assunto Simples";
        String text = "Conteúdo do E-mail";

        CompletableFuture.runAsync(() -> emailService.sendSimpleEmail(to, subject, text))
                .join();

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendPasswordResetEmail_Success() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        String to = "reset@example.com";
        String subject = "Redefinição de Senha";
        String resetLink = "http://localhost:3000/reset?token=abc";

        CompletableFuture.runAsync(() -> emailService.sendPasswordResetEmail(to, subject, resetLink))
                .join();

        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendVerificationEmail_Failure() throws Exception {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        doThrow(new RuntimeException("Erro ao enviar e-mail")).when(mailSender).send(mimeMessage);

        String to = "fail@example.com";
        String subject = "Falha no Envio";
        String verificationLink = "http://localhost:3000/verify?token=fail";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            CompletableFuture.runAsync(() -> emailService.sendVerificationEmail(to, subject, verificationLink))
                    .join();
        });

        assertEquals("Erro ao enviar e-mail", exception.getCause().getMessage());
    }
}
