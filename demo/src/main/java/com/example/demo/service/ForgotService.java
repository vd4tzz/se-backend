package com.example.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.Company;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import jakarta.persistence.EntityExistsException;

@Service
public class ForgotService {

    private UserRepository userRepository;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public ForgotService(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService   = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void sendOtp(String userEmail) throws EntityExistsException {
        User user = userRepository.findByEmail(userEmail)
                                  .orElseThrow(() -> new EntityExistsException("Email not exist"));
                        
        String otp = generateOTP();
        
        user.setOtp(otp);
        user.setOtpGeneratedAt(new Date());
        userRepository.save(user);

        emailService.sendEmail(Company.email, userEmail, "Password reset", otp);
        
    }

    public void validateOtp(String userEmail, String otp) throws RuntimeException {
        User user = userRepository.findByEmail(userEmail)
                                  .orElseThrow(() -> new EntityExistsException("Email not exist"));

        String originalOtp = user.getOtp();
        if(originalOtp.equals(otp) == false) {
            System.out.println(otp);
            throw new RuntimeException("OTP validation failed");
        }
            
        
        Date issueAt = user.getOtpGeneratedAt();
        long timeRemaining = (new Date().getTime()) - issueAt.getTime();
        if(timeRemaining >= 4 * 60 * 1000) {
            System.out.println(timeRemaining);
            throw new RuntimeException("OTP validation failed");
        }
            
    }

    public void resetPassword(String userEmail, String userPassword) throws EntityExistsException {
        User user = userRepository.findByEmail(userEmail)
                                  .orElseThrow(() -> new EntityExistsException("Email not exist"));

        user.setPassword(passwordEncoder.encode(userPassword));

        userRepository.save(user);
    }

    public String generateOTP() {
        return "1234";
    }
}
