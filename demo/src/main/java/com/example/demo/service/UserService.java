package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.PortfolioDto;
import com.example.demo.model.User;
import com.example.demo.repository.PortfolioRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    PortfolioRepository portfolioRepository;

    public List<PortfolioDto> portfolio(String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        int userId = user.getUserId();

        return portfolioRepository.findPortfolioWithCurrentPrice(userId);
    }
}
