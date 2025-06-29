package pl.piomin.services.stocktrader.service.providers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pl.piomin.services.stocktrader.config.ProfitApiProperties;
import pl.piomin.services.stocktrader.config.TwelveDataApiProperties;
import pl.piomin.services.stocktrader.model.StockDailyData;
import pl.piomin.services.stocktrader.model.StockIntradayData;
import pl.piomin.services.stocktrader.model.TimeSeriesResponse;
import pl.piomin.services.stocktrader.service.StockService;

import java.time.Duration;
import java.util.List;

@Service
public class TwelveDataService implements StockService {

    private final RestClient restClient;
    @Value("${twelvedata.api.key:demo}")
    private String apiKey;
    @Autowired
    private TwelveDataApiProperties properties;

    public TwelveDataService() {
        this.restClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    @Override
    public List<StockIntradayData> getIntradayData(String symbol, int limit, Duration interval) {
        return getTimeSeries(symbol, interval.toString(), limit)
                .getValues()
                .stream()
                .map(v -> new StockIntradayData())
                .toList();
    }

    @Override
    public List<StockDailyData> getDailyData(String symbol, int limit) {
        return getTimeSeries(symbol, "1day", limit)
                .getValues()
                .stream()
                .map(v -> new StockDailyData())
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
