package pl.piomin.services.stocktrader.model.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class StockTrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String symbol;
    private String type;
    private double price;
    @Column(name = "date", columnDefinition = "DATE")
    private LocalDate date;
    private String signalType;

    public StockTrade() {

    }

    public StockTrade(String symbol, String type, double price, LocalDate date, String signalType) {
        this.symbol = symbol;
        this.type = type;
        this.price = price;
        this.date = date;
        this.signalType = signalType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSignalType() {
        return signalType;
    }

    public void setSignalType(String signalType) {
        this.signalType = signalType;
    }
}
