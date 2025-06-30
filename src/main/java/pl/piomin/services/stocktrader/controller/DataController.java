package pl.piomin.services.stocktrader.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.piomin.services.stocktrader.model.entity.ShareUpdate;
import pl.piomin.services.stocktrader.model.entity.StockRecord;
import pl.piomin.services.stocktrader.repository.ShareUpdateRepository;
import pl.piomin.services.stocktrader.repository.StockRecordRepository;
import pl.piomin.services.stocktrader.service.providers.StockService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataController {

    private static final Logger LOG = LoggerFactory.getLogger(DataController.class);
    private final StockService stockService;
    private final StockRecordRepository stockRecordRepository;
    private final ShareUpdateRepository shareUpdateRepository;

    public DataController(StockService stockService,
                          StockRecordRepository stockRecordRepository,
                          ShareUpdateRepository shareUpdateRepository) {
        this.stockService = stockService;
        this.stockRecordRepository = stockRecordRepository;
        this.shareUpdateRepository = shareUpdateRepository;
    }

    @GetMapping("/load/{ticker}/{days}/exchange/{exchange}")
    public void loadData(@PathVariable String ticker, @PathVariable int days, @PathVariable String exchange) {
        LOG.info("Loading data from ProfitAPI for {}", ticker);
        var l = stockService.getDailyData(ticker, exchange, LocalDate.now().minusDays(days));
        l.stream().map(phd -> new StockRecord(ticker,
                phd.getOpen(),
                phd.getClose(),
                phd.getHigh(),
                phd.getLow(),
                phd.getVolume(),
                phd.getDate(),
                exchange))
                .forEach(stockRecordRepository::save);
    }

    @GetMapping("/symbol/{symbol}")
    public List<StockRecord> findBySymbol(@PathVariable String symbol) {
        return stockRecordRepository.findBySymbol(symbol);
    }

    @GetMapping("/all")
    public List<StockRecord> findAll() {
        return (List<StockRecord>) stockRecordRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        stockRecordRepository.deleteById(id);
    }

    @GetMapping("/updates")
    public List<ShareUpdate> findAllShareUpdates() {
        return (List<ShareUpdate>) shareUpdateRepository.findAll();
    }

    @DeleteMapping("/updates/{id}")
    public void deleteShareUpdate(@PathVariable Long id) {
        shareUpdateRepository.deleteById(id);
    }

    @DeleteMapping("/updates/all")
    public void deleteAll() {
        shareUpdateRepository.deleteAll();
    }
}
