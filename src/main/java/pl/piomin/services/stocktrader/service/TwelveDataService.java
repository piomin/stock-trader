package pl.piomin.services.stocktrader.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pl.piomin.services.stocktrader.model.TimeSeriesResponse;

@Service
public class TwelveDataService {

    private final RestClient restClient;
    private final String apiKey;

    public TwelveDataService(RestClient restClient, @Value("${twelvedata.api.key:demo}") String apiKey) {
        this.restClient = restClient;
        this.apiKey = apiKey;
    }

    public TimeSeriesResponse getTimeSeries(String symbol, String interval, String outputSize) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/time_series")
                        .queryParam("symbol", symbol)
                        .queryParam("interval", interval)
                        .queryParam("outputsize", outputSize)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .body(TimeSeriesResponse.class);
    }
}
