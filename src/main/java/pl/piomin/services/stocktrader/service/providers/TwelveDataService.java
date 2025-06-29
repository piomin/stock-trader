package pl.piomin.services.stocktrader.service.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;
import pl.piomin.services.stocktrader.config.TwelveDataApiProperties;
import pl.piomin.services.stocktrader.model.StockDailyData;
import pl.piomin.services.stocktrader.model.StockIntradayData;
import pl.piomin.services.stocktrader.model.TimeSeriesResponse;
import pl.piomin.services.stocktrader.service.StockService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class TwelveDataService implements StockService {

    private final RestClient restClient;
    @Value("${twelvedata.api.key:demo}")
    private String apiKey;
    private final TwelveDataApiProperties properties;

    public TwelveDataService(TwelveDataApiProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    @Override
    public List<StockIntradayData> getIntradayData(String symbol, int limit, Duration interval) {
        return getTimeSeries(symbol, interval.toString(), limit)
                .getValues()
                .reversed()
                .stream()
                .map(v -> StockIntradayData.builder()
                        .time(v.getDateTime())
                        .open(Double.parseDouble(v.getOpen()))
                        .close(Double.parseDouble(v.getClose()))
                        .high(Double.parseDouble(v.getHigh()))
                        .low(Double.parseDouble(v.getLow()))
                        .volume(Integer.parseInt(v.getVolume()))
                        .build())
                .toList();
    }

    @Override
    public List<StockDailyData> getDailyData(String symbol, LocalDate startDate) {
        return getTimeSeries(symbol, "1day", (int) ChronoUnit.DAYS.between(LocalDate.now(), startDate))
                .getValues()
                .reversed()
                .stream()
                .map(v -> StockDailyData.builder()
                        .date(v.getDateTime().toLocalDate())
                        .open(Double.parseDouble(v.getOpen()))
                        .close(Double.parseDouble(v.getClose()))
                        .high(Double.parseDouble(v.getHigh()))
                        .low(Double.parseDouble(v.getLow()))
                        .volume(Integer.parseInt(v.getVolume()))
                        .build())
                .toList();
    }

    @Deprecated
    public TimeSeriesResponse getTimeSeries(String symbol, String interval, int limit) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/time_series")
                        .queryParam("symbol", symbol)
                        .queryParam("interval", interval)
                        .queryParam("outputsize", limit)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .body(TimeSeriesResponse.class);
    }
}
