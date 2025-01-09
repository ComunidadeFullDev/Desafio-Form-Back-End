package com.fulldev.formulario.security.domain.controller;


import com.fulldev.formulario.security.domain.model.entity.UserRole;
import com.fulldev.formulario.security.domain.service.TokenService;
import com.fulldev.formulario.security.domain.dto.AuthDTO;
import com.fulldev.formulario.security.domain.dto.LoginResponseDTO;
import com.fulldev.formulario.security.domain.dto.RegisterDTO;
import com.fulldev.formulario.security.domain.model.entity.User;
import com.fulldev.formulario.security.domain.repository.UserRepository;
import com.fulldev.formulario.form.service.EmailService;
import com.fulldev.formulario.security.domain.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthDTO authDTO) {
        try {

            if (authDTO.email() == null || !userService.passwordisValid(authDTO.password()))
                return ResponseEntity.status(403).body("Não foi possível realizar o login do usuário. Existe algum campo obrigatório nulo, vazio ou inválido.");

            User user = (User) userRepository.findByEmail(authDTO.email());

            if (user == null)
                return ResponseEntity.status(403).body("Não existe nehuma conta com esse endereço de email. faça o cadastro e depois volte para o login.");

            if (!user.isVerified())
                return ResponseEntity.status(403).body("Email do usuário não foi verificado. Verifique-o e tente fazer login novamente");

            var emailAndPassword = new UsernamePasswordAuthenticationToken(authDTO.email(), authDTO.password());
            var auth = this.authenticationManager.authenticate(emailAndPassword);

            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (Exception e) {
            return ResponseEntity.status(403).body("Login falhou: " + e.getMessage());
        }
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO registerDTO) {
        try {
            if (this.userRepository.findByEmail(registerDTO.email()) != null)
                return ResponseEntity.badRequest().body("Esse email já está em uso.");

            if (registerDTO.email() == null || !userService.passwordisValid(registerDTO.password()))
                return ResponseEntity.status(403).body("Não foi possível realizar o registro do usuário. Existe algum campo obrigatório nulo, vazio ou inválido.");

            String encryptedPassword = new BCryptPasswordEncoder().encode(registerDTO.password());

            User user = new User(registerDTO.email(), encryptedPassword, UserRole.ADMIN);

            String verificationToken = java.util.UUID.randomUUID().toString();
            user.setVerificationToken(verificationToken);

            String verificationLink = "https://fulldev-seven.vercel.app/verify?token=" + verificationToken;
            emailService.sendVerificationEmail(user.getUsername(), "Confirmação de Cadastro", verificationLink);

            this.userRepository.save(user);

            return ResponseEntity.ok("Usuário registrado. Verifique seu e-mail para ativar sua conta.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping("/verify")
    public ResponseEntity verifyUser(@RequestParam String token) {
        User user = userRepository.findByVerificationToken(token);

        if (user == null) {
            return ResponseEntity.badRequest().body("Token inválido.");
        }

        user.setVerified(true);
        user.setVerificationToken(null);
        emailService.sendVerificationEmail(user.getUsername(), "E-mail Verificado com Sucesso!", null);
        userRepository.save(user);

        return ResponseEntity.ok("Conta verificada com sucesso.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> requestPasswordReset(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        if (email == null || email.isEmpty())
            return ResponseEntity.badRequest().body("O campo email é obrigatório.");

        if (userRepository.findByEmail(email) == null)
            return ResponseEntity.badRequest().body("Não existe um usuário cadastrado com este e-mail.");

        User user = (User) userRepository.findByEmail(email);

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);

        String resetLink = "https://fulldev-seven.vercel.app/reset-password?token=" + resetToken;
        emailService.sendPasswordResetEmail(user.getEmail(), "Redefinição de Senha", resetLink);

        userRepository.save(user);

        return ResponseEntity.ok("Instruções para redefinir sua senha foram enviadas ao seu e-mail.");
    }

    



    @PostMapping("/reset-password/confirm")
    public ResponseEntity confirmPasswordReset(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("password");

        User user = userRepository.findByResetToken(token);

        if (user == null) {
            return ResponseEntity.badRequest().body("Token inválido ou expirado.");
        }

        if (!userService.passwordisValid(newPassword)) {
            return ResponseEntity.badRequest().body("A senha não atende aos critérios de segurança.");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(newPassword);
        user.setPassword(encryptedPassword);
        user.setResetToken(null);

        userRepository.save(user);

        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }



    @DeleteMapping(path = {"/delete/{id}"})
    public ResponseEntity delete(@PathVariable String id, Principal principal){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        if(!(user.getEmail().equals(principal.getName())))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("você não possui permissão para deletar esse usuário");

        return userRepository.findById(id)
                .map(record -> {userRepository.deleteById(id);
                    return ResponseEntity.ok().body(record);
                }).orElse(ResponseEntity.notFound().build());
    }
}
