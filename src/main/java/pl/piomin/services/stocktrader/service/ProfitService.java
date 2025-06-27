package pl.piomin.services.stocktrader.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import pl.piomin.services.stocktrader.config.ProfitApiProperties;
import pl.piomin.services.stocktrader.model.ProfitHistoricalDaily;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Service
public class ProfitService {
    private static final String HISTORICAL_DAILY_ENDPOINT = "/data-api/market-data/historical/daily/{ticker}";
    private static final String HISTORICAL_INTRADAY_ENDPOINT = "/data-api/market-data/historical/intraday/{ticker}";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    
    private final RestClient restClient;
    private final ProfitApiProperties properties;

    public ProfitService(ProfitApiProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    /**
     * Fetches historical daily data for a given ticker
     *
     * @param ticker     the stock ticker symbol (e.g., "AAPL" or "AAPL.US")
     * @param startDate  the start date for the historical data (inclusive), can be null
     * @param endDate    the end date for the historical data (inclusive), can be null
     * @return list of historical daily data points
     */
    public List<ProfitHistoricalDaily> getHistoricalDailyData(String ticker, LocalDateTime startDate, LocalDateTime endDate, String interval) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromPath(HISTORICAL_INTRADAY_ENDPOINT.replace("{ticker}", ticker));

        if (interval != null) {
            uriBuilder.queryParam("interval", interval);
        }
        uriBuilder.queryParam("token", properties.getKey());
        if (startDate != null) {
//            uriBuilder.queryParam("start_date", startDate.format(DATE_FORMATTER));
            uriBuilder.queryParam("start_time", startDate.toEpochSecond(ZoneOffset.UTC));
        }
        if (endDate != null) {
//            uriBuilder.queryParam("end_date", endDate.format(DATE_FORMATTER));
            uriBuilder.queryParam("end_time", endDate.toEpochSecond(ZoneOffset.UTC));
        }

        ProfitHistoricalDaily[] result = restClient.get()
                .uri(uriBuilder.build().toUri())
                .retrieve()
                .onStatus(
                        status -> status == HttpStatus.TOO_MANY_REQUESTS,
                        (request, response) -> {
                            throw new RuntimeException("API rate limit exceeded. Please try again later.");
                        })
                .body(ProfitHistoricalDaily[].class);

        return result != null ? Arrays.asList(result) : List.of();
    }
}
