package pl.piomin.services.stocktrader.model;

import java.time.LocalDate;

public class StockDailyData extends StockData {
    private LocalDate date;

    private StockDailyData() {
        // Private constructor to force using builder
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final StockDailyData stockDailyData;

        private Builder() {
            this.stockDailyData = new StockDailyData();
        }

        public Builder date(LocalDate date) {
            stockDailyData.setDate(date);
            return this;
        }

        public Builder open(double open) {
            stockDailyData.setOpen(open);
            return this;
        }

        public Builder close(double close) {
            stockDailyData.setClose(close);
            return this;
        }

        public Builder high(double high) {
            stockDailyData.setHigh(high);
            return this;
        }

        public Builder low(double low) {
            stockDailyData.setLow(low);
            return this;
        }

        public Builder volume(long volume) {
            stockDailyData.setVolume(volume);
            return this;
        }

        public StockDailyData build() {
            return stockDailyData;
        }
    }
}
