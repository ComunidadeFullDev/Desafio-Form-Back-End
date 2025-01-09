package com.fulldev.formulario.security.infra.config;

import com.fulldev.formulario.security.domain.repository.UserRepository;
import com.fulldev.formulario.security.domain.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        if (token != null){
            var email = tokenService.validantionToken(token);
            UserDetails user = userRepository.findByEmail(email);


            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    public String recoverToken(HttpServletRequest request){
        try {
            var authHeader = request.getHeader("Authorization");
            if (authHeader == null) return null;
            return authHeader.replace("Bearer ", "");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao autorizar o token: "+e);
        }
    }
}
