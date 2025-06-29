package pl.piomin.services.stocktrader.model;

import java.time.LocalDateTime;

public class StockIntradayData extends StockData {
    private LocalDateTime time;

    private StockIntradayData() {
        // Private constructor to force using builder
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }


    public static Builder builder() {
        return new Builder();
    }


    public static class Builder {
        private final StockIntradayData stockIntradayData;

        private Builder() {
            this.stockIntradayData = new StockIntradayData();
        }


        public Builder time(LocalDateTime time) {
            stockIntradayData.setTime(time);
            return this;
        }


        public Builder open(double open) {
            stockIntradayData.setOpen(open);
            return this;
        }


        public Builder close(double close) {
            stockIntradayData.setClose(close);
            return this;
        }


        public Builder high(double high) {
            stockIntradayData.setHigh(high);
            return this;
        }


        public Builder low(double low) {
            stockIntradayData.setLow(low);
            return this;
        }


        public Builder volume(long volume) {
            stockIntradayData.setVolume(volume);
            return this;
        }


        public StockIntradayData build() {
            return stockIntradayData;
        }
    }
}
