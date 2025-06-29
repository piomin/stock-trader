package pl.piomin.services.stocktrader.model;

import java.time.LocalDateTime;

public class StockIntradayData extends StockData {
    private LocalDateTime time;

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
