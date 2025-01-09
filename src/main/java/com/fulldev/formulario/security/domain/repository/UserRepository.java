package com.fulldev.formulario.security.domain.repository;

import com.fulldev.formulario.security.domain.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    UserDetails findByEmail(String email);
    User findByVerificationToken(String token);
    User findByResetToken(String token);
}
