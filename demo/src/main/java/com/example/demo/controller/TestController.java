package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.PortfolioDto;
import com.example.demo.model.Portfolio;
import com.example.demo.model.Stock;
import com.example.demo.model.StockCurrentPrice;
import com.example.demo.model.User;
import com.example.demo.repository.PortfolioRepository;
import com.example.demo.repository.StockCurrentPriceRepository;
import com.example.demo.repository.StockRepository;
import com.example.demo.repository.UserRepository;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    StockRepository stockRepository;

    @Autowired
    StockCurrentPriceRepository stockCurrentPriceRepository;

    @GetMapping("/api")
    public List<User> test() {
        return userRepository.findAll();
    }

    @GetMapping("/test2")
    public List<Portfolio> test2() {
        return portfolioRepository.findAll();
    }

    @PostMapping("/test3")
    public List<Stock> test3() {
        return stockRepository.findAll();
    }

    @GetMapping("/test4")
    public List<StockCurrentPrice> test4() {
        return stockCurrentPriceRepository.findAll();
    }
    
    @GetMapping("/test5")
    public List<PortfolioDto> test5() {
        return portfolioRepository.findPortfolioWithCurrentPrice(19);
    }
}
