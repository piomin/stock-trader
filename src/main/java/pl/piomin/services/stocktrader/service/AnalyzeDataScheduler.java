package pl.piomin.services.stocktrader.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.ta4j.core.*;
import org.ta4j.core.num.DecimalNum;
import pl.piomin.services.stocktrader.repository.ShareUpdateRepository;
import pl.piomin.services.stocktrader.repository.StockRecordRepository;
import pl.piomin.services.stocktrader.strategy.BuildAndRunStrategy;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Set;

@Service
public class AnalyzeDataScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyzeDataScheduler.class);
    private final StockRecordRepository repository;
    private final ShareUpdateRepository shareUpdateRepository;
    private final Set<BuildAndRunStrategy> runnableStrategies;

    public AnalyzeDataScheduler(StockRecordRepository repository,
                                ShareUpdateRepository shareUpdateRepository,
                                Set<BuildAndRunStrategy> runnableStrategies) {
        this.repository = repository;
        this.shareUpdateRepository = shareUpdateRepository;
        this.runnableStrategies = runnableStrategies;
    }

    /**
     * Scheduled task that runs once per hour at the beginning of the hour
     */
    @Scheduled(cron = "${app.scheduler.analyze}")
    public void updateStockDataHourly() {
        LOG.info("Starting scheduled stock data update");
        try {
            shareUpdateRepository.findAll().forEach(it -> run(it.getSymbol()));
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

        repository.findBySymbol(symbol)
                .forEach(record -> {
                    series.addBar(buildBar(record.getDate(), record.getOpen(), record.getClose(), record.getHigh(), record.getLow(), record.getVolume()));
                });

        runnableStrategies.forEach(it -> {
            TradingRecord tradingRecord = it.buildAndRun(series);
            Trade lastEntry = tradingRecord.getLastEntry();
            if (lastEntry != null) {
                Bar b = series.getBar(lastEntry.getIndex());
                boolean isRecent = b.getEndTime().isAfter(LocalDate.now().minusDays(16).atStartOfDay().toInstant(ZoneOffset.UTC));
                if (isRecent) {
                    LOG.info("[{}] Last entry: {} - Bar: {}", symbol, lastEntry, series.getBar(lastEntry.getIndex()));
                }
            }
        });

    }
}
