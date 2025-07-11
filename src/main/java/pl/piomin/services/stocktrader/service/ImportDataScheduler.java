package pl.piomin.services.stocktrader.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.piomin.services.stocktrader.model.entity.ShareUpdate;
import pl.piomin.services.stocktrader.model.entity.StockRecord;
import pl.piomin.services.stocktrader.repository.ShareUpdateRepository;
import pl.piomin.services.stocktrader.repository.StockRecordRepository;
import pl.piomin.services.stocktrader.service.providers.StockService;

import java.time.LocalDate;

@Service
public class ImportDataScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(ImportDataScheduler.class);
    private final StockRecordRepository repository;
    private final ShareUpdateRepository shareUpdateRepository;
    private final StockService stockService;

    public ImportDataScheduler(StockRecordRepository repository,
                             ShareUpdateRepository shareUpdateRepository,
                             StockService stockService) {
        this.repository = repository;
        this.shareUpdateRepository = shareUpdateRepository;
        this.stockService = stockService;
    }

    @Scheduled(cron = "${app.scheduler.import}")
    public void importStockData() {
        LOG.info("Starting scheduled stock data import");
        try {
            shareUpdateRepository.findAll().forEach(this::run);
            LOG.info("Completed stock data import");
        } catch (Exception e) {
            LOG.error("Error during scheduled stock data import", e);
        }
    }

    public void run(ShareUpdate shareUpdate) {
        LOG.info("Importing data for symbol: {}", shareUpdate.getSymbol());
        LocalDate startDate;
        StockRecord record = repository.findFirstBySymbolOrderByDateDesc(shareUpdate.getSymbol());
        LOG.info("Latest record found: {}", record);
        if (record == null) {
            startDate = LocalDate.now().minusDays(365);
        } else {
            startDate = record.getDate().plusDays(1);
        }

        var l = stockService.getDailyData(shareUpdate.getSymbol(), shareUpdate.getExchange(), startDate);
        LOG.info("Number of records: {}", l.size());
        l.stream().map(phd -> new StockRecord(shareUpdate.getSymbol(),
                phd.getOpen(),
                phd.getClose(),
                phd.getHigh(),
                phd.getLow(),
                phd.getVolume(),
                phd.getDate(),
                shareUpdate.getExchange()))
                .forEach(repository::save);
        shareUpdate.setLastUpdate(LocalDate.now());
        shareUpdateRepository.save(shareUpdate);
    }
}
