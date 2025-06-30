package pl.piomin.services.stocktrader.model.providers.profit;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ProfitHistoricalDaily {
    @JsonProperty("t")
    private Long timestamp;
    
    @JsonProperty("o")
    private Double open;
    
    @JsonProperty("h")
    private Double high;
    
    @JsonProperty("l")
    private Double low;
    
    @JsonProperty("c")
    private Double close;
    
    @JsonProperty("v")
    private Long volume;
    
    @JsonProperty("a")
    private Double adjustedClose;

    // Getters and Setters
    public LocalDateTime getDateTime() {
        return timestamp != null ? 
            LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), ZoneId.systemDefault()) : 
            null;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    public Double getAdjustedClose() {
        return adjustedClose;
    }

    public void setAdjustedClose(Double adjustedClose) {
        this.adjustedClose = adjustedClose;
    }

    @Override
    public String toString() {
        return "ProfitHistoricalDaily{" +
                "dateTime=" + getDateTime() +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                ", adjustedClose=" + adjustedClose +
                '}';
    }
}
