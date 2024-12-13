package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.Dto.PortfolioDto;
import com.example.demo.model.Portfolio;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {
    List<Portfolio> findByUser_UserId(int userId);

    @Query("SELECT new com.example.demo.Dto.PortfolioDto(c.stock.symbol, p.purchaseAt, p.quantity, p.purchasePrice, c.currentPrice) " +
            "FROM Portfolio p " +
            "JOIN StockCurrentPrice c ON p.stock.stockId = c.stock.stockId " +
            "WHERE p.user.userId = :userId")
    List<PortfolioDto> findPortfolioWithCurrentPrice(int userId);
}
