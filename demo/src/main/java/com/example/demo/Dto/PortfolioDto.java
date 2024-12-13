package com.example.demo.Dto;

import java.util.Date;

public class PortfolioDto {
    private String symbol;
    private Date date;
    private int amount;
    private double purchase;
    private double current;

    public PortfolioDto(String symbol, Date date, int amount, double purchase, double current) {
        this.symbol = symbol;
        this.date = date;
        this.amount = amount;
        this.purchase = purchase;
        this.current = current;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPurchase() {
        return this.purchase;
    }

    public void setPurchase(double purchase) {
        this.purchase = purchase;
    }

    public double getCurrent() {
        return this.current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }
}
