package pl.piomin.services.stocktrader.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.piomin.services.stocktrader.model.StockRecord;
import pl.piomin.services.stocktrader.repository.StockRecordRepository;
import pl.piomin.services.stocktrader.service.StockService;
import pl.piomin.services.stocktrader.service.providers.ProfitService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DataController {

    private static final Logger LOG = LoggerFactory.getLogger(DataController.class);
    private final StockService stockService;
    private final StockRecordRepository repository;

    public DataController(StockService stockService,
                          StockRecordRepository repository) {
        this.stockService = stockService;
        this.repository = repository;
    }

    @GetMapping("/load/{ticker}/{days}/exchange/{exchange}")
    public void loadData(@PathVariable String ticker, @PathVariable int days, @PathVariable String exchange) {
        LOG.info("Loading data from ProfitAPI for {}", ticker);
        var l = stockService.getDailyData(ticker + "." + exchange, LocalDate.now().minusDays(days));
        l.stream().map(phd -> new StockRecord(ticker,
                phd.getOpen(),
                phd.getClose(),
                phd.getHigh(),
                phd.getLow(),
                phd.getVolume(),
                phd.getDate(),
                exchange))
                .forEach(repository::save);
    }

    @GetMapping("/symbol/{symbol}")
    public List<StockRecord> findBySymbol(@PathVariable String symbol) {
        return repository.findBySymbol(symbol);
    }

    @GetMapping("/all")
    public List<StockRecord> findAll() {
        return (List<StockRecord>) repository.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
