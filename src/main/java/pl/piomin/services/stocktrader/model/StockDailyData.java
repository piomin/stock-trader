package pl.piomin.services.stocktrader.model;

import java.time.LocalDate;

public class StockDailyData extends StockData {
    private LocalDate date;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
