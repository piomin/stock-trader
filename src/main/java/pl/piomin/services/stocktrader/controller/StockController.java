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
import pl.piomin.services.stocktrader.model.TimeSeriesResponse;
import pl.piomin.services.stocktrader.service.providers.ProfitService;
import pl.piomin.services.stocktrader.service.providers.TwelveDataService;

import java.time.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    private static final Logger LOG = LoggerFactory.getLogger(StockController.class);

    private final TwelveDataService twelveDataService;
    private final ProfitService profitService;

    public StockController(TwelveDataService twelveDataService,
                           ProfitService profitService) {
        this.twelveDataService = twelveDataService;
        this.profitService = profitService;
    }

    @GetMapping("/{symbol}/time-series")
    public String getTimeSeries(
            @PathVariable String symbol,
            @RequestParam(defaultValue = "1day") String interval,
            @RequestParam(defaultValue = "30") String outputSize) {

        TimeSeriesResponse response = twelveDataService
                .getTimeSeries(symbol, interval, Integer.parseInt(outputSize));
//        List<ProfitHistoricalDaily> values = profitService
//                .getHistoricalIntradayData(symbol, LocalDateTime.now().minusDays(3), LocalDateTime.now(), interval);
        BarSeries series = new BaseBarSeriesBuilder()
                .withName(symbol)
                .build();

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        RSIIndicator rsi = new RSIIndicator(closePrice, 14);

        Rule entryRule = new CrossedDownIndicatorRule(rsi, 25);
        Rule exitRule = new CrossedUpIndicatorRule(rsi, 75);
        Strategy strategy = new BaseStrategy(entryRule, exitRule);

        AtomicInteger i = new AtomicInteger();
        response.getValues().reversed().forEach(value -> {
            series.addBar(buildBar(value.getDateTime(),
                    value.getOpen(),
                    value.getClose(),
                    value.getHigh(),
                    value.getLow(),
                    value.getVolume()));
            LOG.info("Added: {}:{} -> {}", i.incrementAndGet(), value.getDateTime(), value.getClose());
        });
//        values.reversed().forEach(value -> {
//            series.addBar(buildBar2(value.getDateTime(),
//                    value.getOpen(),
//                    value.getClose(),
//                    value.getHigh(),
//                    value.getLow(),
//                    value.getVolume()));
//            LOG.info("Added: {}:{} -> {}", i.incrementAndGet(), value.getDateTime(), value.getClose());
//        });

        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy);
//        LOG.info("Trading records: {}", tradingRecord.getPositionCount());
        LOG.info("Trading records: {}", tradingRecord.getTrades());

        var n = new ProfitCriterion().calculate(series, tradingRecord);
        return "OK: " + n.floatValue();
    }

    BaseBar buildBar(LocalDateTime time, String open, String close, String high, String low, String volume) {
        return new BaseBar(Duration.ofMinutes(5),
//                Instant.now(),
                time.toInstant(ZoneOffset.UTC),
                DecimalNum.valueOf(open),
                DecimalNum.valueOf(close),
                DecimalNum.valueOf(high),
                DecimalNum.valueOf(low),
                DecimalNum.valueOf(volume),
                DecimalNum.valueOf("0"), 0L);
    }

    BaseBar buildBar2(LocalDateTime time, Double open, Double close, Double high, Double low, Long volume) {
        return new BaseBar(Duration.ofMinutes(5),
//                Instant.now(),
                time.toInstant(ZoneOffset.UTC),
                DecimalNum.valueOf(open),
                DecimalNum.valueOf(close),
                DecimalNum.valueOf(high),
                DecimalNum.valueOf(low),
                DecimalNum.valueOf(volume),
                DecimalNum.valueOf("0"), 0L);
    }
}
