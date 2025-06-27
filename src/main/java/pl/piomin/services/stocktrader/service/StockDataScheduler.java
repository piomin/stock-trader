package pl.piomin.services.stocktrader.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.ta4j.core.*;
import org.ta4j.core.backtest.BarSeriesManager;
import org.ta4j.core.criteria.pnl.ProfitCriterion;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.rules.CrossedDownIndicatorRule;
import org.ta4j.core.rules.CrossedUpIndicatorRule;
import pl.piomin.services.stocktrader.repository.StockRecordRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StockDataScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(StockDataScheduler.class);
    private final StockRecordRepository repository;

    public StockDataScheduler(StockRecordRepository repository) {
        this.repository = repository;
    }

    @Value("${stock.symbols}")
    String symbols;
    /**
     * Scheduled task that runs once per hour at the beginning of the hour
     */
    @Scheduled(cron = "0 * * * * *") // Runs at the start of every minute
    public void updateStockDataHourly() {
        LOG.info("Starting scheduled stock data update");
        try {
            Arrays.stream(symbols.split(",")).forEach(this::run);
            LOG.info("Completed stock data update");
        } catch (Exception e) {
            LOG.error("Error during scheduled stock data update", e);
        }
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

    public void run(String symbol) {
        BarSeries series = new BaseBarSeriesBuilder()
                .withName(symbol)
                .build();

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        RSIIndicator rsi = new RSIIndicator(closePrice, 14);

        Rule entryRule = new CrossedDownIndicatorRule(rsi, 30);
        Rule exitRule = new CrossedUpIndicatorRule(rsi, 75);
        Strategy strategy = new BaseStrategy(entryRule, exitRule);

//        AtomicInteger i = new AtomicInteger();
        repository.findBySymbol(symbol)
                .forEach(record -> {
//                    LOG.info("Record({}): {}", i.getAndIncrement(), record);
                    series.addBar(buildBar(record.getDate(), record.getOpen(), record.getClose(), record.getHigh(), record.getLow(), record.getVolume()));
                });

        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy);
//        LOG.info("Trading records: {}", tradingRecord.getPositionCount());
//        LOG.info("Trading records: {}", tradingRecord.getTrades());

        Trade lastEntry = tradingRecord.getLastEntry();
        if (lastEntry != null) {
            LOG.info("[{}] Last entry: {} - Bar: {}", symbol, lastEntry, series.getBar(lastEntry.getIndex()));
        }
    }
}
