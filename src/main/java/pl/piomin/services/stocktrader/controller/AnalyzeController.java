package pl.piomin.services.stocktrader.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ta4j.core.*;
import org.ta4j.core.backtest.BarSeriesManager;
import org.ta4j.core.criteria.pnl.ProfitCriterion;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.DoubleNum;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import pl.piomin.services.stocktrader.repository.StockRecordRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/analyze")
public class AnalyzeController {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyzeController.class);
    private final StockRecordRepository repository;

    public AnalyzeController(StockRecordRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/rsi/{symbol}/days/{days}")
    public String rsiStrategy(@PathVariable String symbol, @PathVariable int days) {
        LOG.info("RSI strategy");

        BarSeries series = new BaseBarSeriesBuilder()
                .withName(symbol)
                .build();

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        RSIIndicator rsi = new RSIIndicator(closePrice, 14);

        Rule entryRule = new CrossedDownIndicatorRule(rsi, 30);
        Rule exitRule = new CrossedUpIndicatorRule(rsi, 75);
        Strategy strategy = new BaseStrategy(entryRule, exitRule);

        AtomicInteger i = new AtomicInteger();
        repository.findBySymbol(symbol)
                .forEach(record -> {
                    LOG.info("Record({}): {}", i.getAndIncrement(), record);
                    series.addBar(buildBar(record.getDate(), record.getOpen(), record.getClose(), record.getHigh(), record.getLow(), record.getVolume()));
                });

        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy);
        LOG.info("Trading records: {}", tradingRecord.getPositionCount());
        LOG.info("Trading records: {}", tradingRecord.getTrades());

        int index = tradingRecord.getLastEntry().getIndex();
        LOG.info("Bar: {}", series.getBar(index));

        var n = new ProfitCriterion().calculate(series, tradingRecord);
        return "Income: " + n.floatValue();
    }

    BaseBar buildBar(LocalDate date, Double open, Double close, Double high, Double low, Long volume) {
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
