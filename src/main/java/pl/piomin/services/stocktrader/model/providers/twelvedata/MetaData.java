package pl.piomin.services.stocktrader.model.providers.twelvedata;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class MetaData {
    private String symbol;
    private String interval;
    private String currency;
    @JsonProperty("exchange_timezone")
    private String exchangeTimezone;
    private String exchange;
    @JsonProperty("mic_code")
    private String micCode;
    private String type;

    public MetaData() {
    }

    public MetaData(String symbol, String interval, String currency, String exchangeTimezone, 
                   String exchange, String micCode, String type) {
        this.symbol = symbol;
        this.interval = interval;
        this.currency = currency;
        this.exchangeTimezone = exchangeTimezone;
        this.exchange = exchange;
        this.micCode = micCode;
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExchangeTimezone() {
        return exchangeTimezone;
    }

    public void setExchangeTimezone(String exchangeTimezone) {
        this.exchangeTimezone = exchangeTimezone;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getMicCode() {
        return micCode;
    }

    public void setMicCode(String micCode) {
        this.micCode = micCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaData metaData = (MetaData) o;
        return Objects.equals(symbol, metaData.symbol) && 
               Objects.equals(interval, metaData.interval) && 
               Objects.equals(currency, metaData.currency) && 
               Objects.equals(exchangeTimezone, metaData.exchangeTimezone) && 
               Objects.equals(exchange, metaData.exchange) && 
               Objects.equals(micCode, metaData.micCode) && 
               Objects.equals(type, metaData.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, interval, currency, exchangeTimezone, exchange, micCode, type);
    }

    @Override
    public String toString() {
        return "MetaData{" +
                "symbol='" + symbol + '\'' +
                ", interval='" + interval + '\'' +
                ", currency='" + currency + '\'' +
                ", exchangeTimezone='" + exchangeTimezone + '\'' +
                ", exchange='" + exchange + '\'' +
                ", micCode='" + micCode + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
