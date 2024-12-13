package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.UserEmailDto;
import com.example.demo.Dto.UserLoginDto;
import com.example.demo.Dto.UserPasswordResetDto;
import com.example.demo.Dto.UserSignupDto;
import com.example.demo.Dto.UserValidateOtp;
import com.example.demo.service.ForgotService;
import com.example.demo.service.JwtUtils;
import com.example.demo.service.SignupService;

import jakarta.persistence.EntityExistsException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
    private SignupService signupService;
    private ForgotService forgotService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtils jwtUtils,
                          SignupService signupService,
                          ForgotService forgotService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.signupService = signupService;
        this.forgotService = forgotService;
    }

    @PostMapping("/test")
    public ResponseEntity<Object> test() {
        return ResponseEntity.status(HttpStatus.OK).body("Testing <3");
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserLoginDto user) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication userAuthentication = null;

        try {
            userAuthentication = authenticationManager.authenticate(token);
        } catch(AuthenticationException e) {
            System.out.println(e);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }

        String username = userAuthentication.getName();
        String role = userAuthentication.getAuthorities().iterator().next().getAuthority();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Access-Control-Expose-Headers", "Authorization, " + "Set-Cookie");
        httpHeaders.add("Authorization", "Bearer " + jwtUtils.generateToken(username, role));

        return ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body("Authentication success");
    }

    @PostMapping("/signup") 
    public ResponseEntity<Object> signup(@RequestBody UserSignupDto user) {
        Map<String, String> errors = new HashMap<>();
        try {
            signupService.isUserNotExist(user.getUsername());
        } catch(EntityExistsException e) {
            
            errors.put("error", "username existed");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
        }

        try {
            signupService.isEmailNotExist(user.getEmail());
        } catch(EntityExistsException e) {
            errors.put("error", "email existed");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errors);
        }

        signupService.signup(user.getUsername(), user.getEmail(), user.getPassword());

        return ResponseEntity.status(HttpStatus.OK).body("Sign-up success");
    }

    @PostMapping("/forgot")
    public ResponseEntity<Object> forgot(@RequestBody UserEmailDto user) {
        try {
            forgotService.sendOtp(user.getEmail());
        } catch(EntityExistsException e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not exist");
        }

        return ResponseEntity.status(HttpStatus.OK).body("OPT sent through email");
    }

    @PostMapping("/forgot/otp")
    public ResponseEntity<Object> validateOtp(@RequestBody UserValidateOtp user) {
        try {
            forgotService.validateOtp(user.getEmail(), user.getOtp());
        } catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Otp validation failed");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Otp validation success");
    }

    @PostMapping("/forgot/reset")
    public ResponseEntity<Object> resetPassword(@RequestBody UserPasswordResetDto user) {
        try {
            forgotService.resetPassword(user.getEmail(), user.getPassword());
        } catch(EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password reset failed");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Password reset success");
    }
}
