package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.StockCurrentPrice;

@Repository
public interface StockCurrentPriceRepository extends JpaRepository<StockCurrentPrice, Integer> {
    
}
