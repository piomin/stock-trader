package pl.piomin.services.stocktrader.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.ta4j.core.*;
import org.ta4j.core.backtest.BarSeriesManager;
import org.ta4j.core.criteria.pnl.ProfitCriterion;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import pl.piomin.services.stocktrader.model.StockDailyData;
import pl.piomin.services.stocktrader.model.StockIntradayData;
import pl.piomin.services.stocktrader.service.providers.StockService;

import java.time.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private static final Logger LOG = LoggerFactory.getLogger(StockController.class);
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/{symbol}/daily/rsi")
    public String getDailyRSI(@PathVariable String symbol,
                              @RequestParam(defaultValue = "300") int limit,
                              @RequestParam(defaultValue = "WAR") String exchange) {

        List<StockDailyData> response = stockService
                .getDailyData(symbol, exchange, LocalDate.now().minusDays(limit));

        BarSeries series = new BaseBarSeriesBuilder()
                .withName(symbol)
                .build();

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        RSIIndicator rsi = new RSIIndicator(closePrice, 14);

        Rule entryRule = new CrossedDownIndicatorRule(rsi, 25);
        Rule exitRule = new CrossedUpIndicatorRule(rsi, 75);
        Strategy strategy = new BaseStrategy(entryRule, exitRule);

        AtomicInteger i = new AtomicInteger();
        response.forEach(value -> {
            series.addBar(buildDailyBar(value.getDate(),
                    value.getOpen(),
                    value.getClose(),
                    value.getHigh(),
                    value.getLow(),
                    value.getVolume()));
            LOG.info("Added: {}:{} -> {}", i.incrementAndGet(), value.getDate(), value.getClose());
        });

        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy);
//        LOG.info("Trading records: {}", tradingRecord.getPositionCount());
        LOG.info("Trading records: {}", tradingRecord.getTrades());

        var n = new ProfitCriterion().calculate(series, tradingRecord);
        return "OK: " + n.floatValue();
    }

    BaseBar buildDailyBar(LocalDate date, Double open, Double close, Double high, Double low, Long volume) {
        return new BaseBar(Duration.ofDays(1),
                date.atStartOfDay().toInstant(ZoneOffset.UTC),
                DecimalNum.valueOf(open),
                DecimalNum.valueOf(close),
                DecimalNum.valueOf(high),
                DecimalNum.valueOf(low),
                DecimalNum.valueOf(volume),
                DecimalNum.valueOf("0"), 0L);
    }
}
