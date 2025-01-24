package com.fulldev.formulario.security.repository;

import com.fulldev.formulario.security.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    UserDetails findByEmail(String email);
    User findByVerificationToken(String token);
    User findByResetToken(String token);
}
