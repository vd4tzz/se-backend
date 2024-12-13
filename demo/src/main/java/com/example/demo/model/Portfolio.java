package com.example.demo.model;

import java.util.Date;

import jakarta.persistence.*;;

@Entity
@Table(name = "portfolio")
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "portfolio_id")
    private int portfolioId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(name = "quantity")
    private int quantity;
    
    @Column(name = "purchase_price")
    private double purchasePrice;

    @Column(name = "purchase_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date purchaseAt;


    public int getPortfolioId() {
        return this.portfolioId;
    }

    public void setPortfolioId(int portfolioId) {
        this.portfolioId = portfolioId;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Stock getStock() {
        return this.stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPurchasePrice() {
        return this.purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public Date getPurchaseAt() {
        return this.purchaseAt;
    }

    public void setPurchaseAt(Date purchaseAt) {
        this.purchaseAt = purchaseAt;
    }
    

    
}
