package pl.piomin.services.stocktrader.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ta4j.core.*;
import org.ta4j.core.criteria.pnl.ProfitCriterion;
import org.ta4j.core.num.DecimalNum;
import pl.piomin.services.stocktrader.model.entity.StockTrade;
import pl.piomin.services.stocktrader.repository.StockRecordRepository;
import pl.piomin.services.stocktrader.repository.StockTradeRepository;
import pl.piomin.services.stocktrader.strategy.BuildAndRunStrategy;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/analyze")
public class AnalyzeController {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyzeController.class);
    private final StockRecordRepository repository;
    private final StockTradeRepository stockTradeRepository;
    private final Set<BuildAndRunStrategy> runnableStrategies;

    public AnalyzeController(StockRecordRepository repository,
                             StockTradeRepository stockTradeRepository,
                             Set<BuildAndRunStrategy> runnableStrategies) {
        this.repository = repository;
        this.stockTradeRepository = stockTradeRepository;
        this.runnableStrategies = runnableStrategies;
    }

    @GetMapping("/{symbol}/all")
    public String allStrategies(@PathVariable String symbol) {
        BarSeries series = new BaseBarSeriesBuilder()
                .withName(symbol)
                .build();

        AtomicInteger i = new AtomicInteger();
        repository.findBySymbol(symbol)
                .forEach(record -> {
                    LOG.info("Record({}): {}", i.getAndIncrement(), record);
                    series.addBar(buildBar(record.getDate(), record.getOpen(), record.getClose(), record.getHigh(), record.getLow(), record.getVolume()));
                });

        runnableStrategies.forEach(it -> {
            TradingRecord tradingRecord = it.buildAndRun(series);
            LOG.info("Trading records: {}", tradingRecord.getPositionCount());
            LOG.info("Trading records: {}", tradingRecord.getTrades());
            if (tradingRecord.getLastEntry() != null) {
                int index = tradingRecord.getLastEntry().getIndex();
                LOG.info("Bar: {}", series.getBar(index));

                var n = new ProfitCriterion().calculate(series, tradingRecord);
                LOG.info("Profit({}): {}", it.getClass().getSimpleName(), n.floatValue());
            }
            tradingRecord.getTrades().forEach(t -> {
                StockTrade trade = stockTradeRepository.findBySymbolAndTypeAndDate(symbol, t.getType().name(), LocalDate.ofInstant(series.getBar(t.getIndex()).getEndTime(), ZoneOffset.UTC));
                if (trade == null) {
                    stockTradeRepository.save(
                            new StockTrade(symbol,
                                    t.getType().name(),
                                    t.getValue().doubleValue(),
                                    LocalDate.ofInstant(series.getBar(t.getIndex()).getEndTime(), ZoneOffset.UTC),
                                    it.getClass().getSimpleName())
                    );
                }
                LOG.info("Trade: {}", t);
            });
        });

        return "Income";
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
