package pl.piomin.services.stocktrader.service.providers;

import pl.piomin.services.stocktrader.model.StockDailyData;
import pl.piomin.services.stocktrader.model.StockIntradayData;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public interface StockService {
    List<StockIntradayData> getIntradayData(String symbol, int limit, Duration interval);
    List<StockDailyData> getDailyData(String symbol, LocalDate startDate);
}
