package com.fulldev.formulario.security.infra.config;

import com.fulldev.formulario.security.domain.model.entity.User;
import com.fulldev.formulario.security.domain.model.entity.UserRole;
import com.fulldev.formulario.security.domain.repository.UserRepository;
import com.fulldev.formulario.security.domain.service.TokenService;
import com.fulldev.formulario.security.domain.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class OtherLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public OtherLoginSuccessHandler(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = (User) userRepository.findByEmail(email);
        if (user == null) {
            System.out.println("usuário era nullo");
            user = new User(email, UUID.randomUUID().toString(), UserRole.ADMIN);
            user.setVerified(true);
            userRepository.save(user);
        }

        String jwtToken = tokenService.generateToken(user);

        Cookie jwtCookie = new Cookie("token", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(jwtCookie);

        response.sendRedirect("https://fulldev-seven.vercel.app/workspace");
    }

}
