package pl.piomin.services.stocktrader.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

public class TimeSeriesData {
    @JsonProperty("datetime")
    private LocalDateTime dateTime;
    private String open;
    private String high;
    private String low;
    private String close;
    private String volume;

    public TimeSeriesData() {
    }

    public TimeSeriesData(LocalDateTime dateTime, String open, String high, String low, String close, String volume) {
        this.dateTime = dateTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSeriesData that = (TimeSeriesData) o;
        return Objects.equals(dateTime, that.dateTime) && 
               Objects.equals(open, that.open) && 
               Objects.equals(high, that.high) && 
               Objects.equals(low, that.low) && 
               Objects.equals(close, that.close) && 
               Objects.equals(volume, that.volume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime, open, high, low, close, volume);
    }

    @Override
    public String toString() {
        return "TimeSeriesData{" +
                "dateTime=" + dateTime +
                ", open='" + open + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", close='" + close + '\'' +
                ", volume='" + volume + '\'' +
                '}';
    }
}
