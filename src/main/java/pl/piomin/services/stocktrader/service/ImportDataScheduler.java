package pl.piomin.services.stocktrader.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.piomin.services.stocktrader.model.ShareUpdate;
import pl.piomin.services.stocktrader.model.StockRecord;
import pl.piomin.services.stocktrader.repository.ShareUpdateRepository;
import pl.piomin.services.stocktrader.repository.StockRecordRepository;

import java.time.LocalDate;

@Service
public class ImportDataScheduler {

    private static final Logger LOG = LoggerFactory.getLogger(StockDataScheduler.class);
    private final StockRecordRepository repository;
    private final ShareUpdateRepository shareUpdateRepository;
    private final StockService stockService;
//    private final ProfitService profitService;

    public ImportDataScheduler(StockRecordRepository repository,
                               ShareUpdateRepository shareUpdateRepository,
//                               ProfitService profitService,
                               StockService stockService) {
        this.repository = repository;
        this.shareUpdateRepository = shareUpdateRepository;
//        this.profitService = profitService;
        this.stockService = stockService;
    }

    @Scheduled(cron = "0 * * * * *")
    public void importStockData() {
        LOG.info("Starting scheduled stock data update");
        try {
            shareUpdateRepository.findAll().forEach(it -> run(it));
            LOG.info("Completed stock data update");
        } catch (Exception e) {
            LOG.error("Error during scheduled stock data update", e);
        }
    }

    public void run(ShareUpdate shareUpdate) {
        LOG.info("Importing data for symbol: {}", shareUpdate.getSymbol());
        StockRecord record = repository.findFirstBySymbolOrderByDateDesc(shareUpdate.getSymbol());
        stockService.getDailyData(shareUpdate.getSymbol(), record.getDate().plusDays(1))
                .stream().map(phd -> new StockRecord(shareUpdate.getSymbol(),
                phd.getOpen(),
                phd.getClose(),
                phd.getHigh(),
                phd.getLow(),
                phd.getVolume(),
                phd.getDate(),
                "WAR"))
                .forEach(repository::save);
        shareUpdate.setLastUpdate(LocalDate.now());
        shareUpdateRepository.save(shareUpdate);
    }
}
