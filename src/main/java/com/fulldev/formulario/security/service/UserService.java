package com.fulldev.formulario.security.service;

import com.fulldev.formulario.security.model.entity.User;
import com.fulldev.formulario.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public boolean passwordisValid(String password){
        if (password == null || password.isEmpty()){
            return false;
        }else {
            return true;
        }
    }

}
