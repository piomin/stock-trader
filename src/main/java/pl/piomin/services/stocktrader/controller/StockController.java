package pl.piomin.services.stocktrader.controller;

import org.springframework.web.bind.annotation.*;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BarSeriesBuilder;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;
import pl.piomin.services.stocktrader.model.TimeSeriesResponse;
import pl.piomin.services.stocktrader.service.TwelveDataService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private final TwelveDataService twelveDataService;

    public StockController(TwelveDataService twelveDataService) {
        this.twelveDataService = twelveDataService;
    }

    @GetMapping("/{symbol}/time-series")
    public String getTimeSeries(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "1day") String interval,
            @RequestParam(defaultValue = "30") String outputSize) {

        TimeSeriesResponse response = twelveDataService
                .getTimeSeries(symbol, interval, outputSize);
        BarSeries series = new BaseBarSeriesBuilder()
                .withName(symbol)
                .build();
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        RSIIndicator rsi = new RSIIndicator(closePrice, 14);

        response.getValues().forEach(value -> {
            series.addBar(buildBar(value.getDateTime(),
                    value.getOpen(),
                    value.getClose(),
                    value.getHigh(),
                    value.getLow(),
                    value.getVolume()));

        });

        return "OK";
    }

    BaseBar buildBar(LocalDateTime time, String open, String close, String high, String low, String volume) {
        return new BaseBar(Duration.ofMinutes(5L),
                time.toInstant(ZoneOffset.UTC),
                DecimalNum.valueOf(open),
                DecimalNum.valueOf(close),
                DecimalNum.valueOf(high),
                DecimalNum.valueOf(low),
                DecimalNum.valueOf(volume),
                DecimalNum.valueOf("0"), 0L);
    }
}
