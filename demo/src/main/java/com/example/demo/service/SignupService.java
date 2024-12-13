package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import jakarta.persistence.EntityExistsException;

@Service
public class SignupService {
    
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public SignupService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signup(String username, String email, String password) throws EntityExistsException {
        isUserNotExist(username);
        isEmailNotExist(email);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setEmail(email);
        newUser.setRole("user");

        userRepository.save(newUser);
    }

    public void isUserNotExist(String username) throws EntityExistsException {
        boolean isEmpty = userRepository.findByUsername(username).isEmpty();
        if(!isEmpty)
            throw new EntityExistsException("Username existed");
    }

    public void isEmailNotExist(String email) throws EntityExistsException {
        boolean isEmpty = userRepository.findByEmail(email).isEmpty();
        if(!isEmpty)
            throw new EntityExistsException("Email existed");
    }
}
