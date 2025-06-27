package pl.piomin.services.stocktrader.repository;

import org.springframework.data.repository.CrudRepository;
import pl.piomin.services.stocktrader.model.StockRecord;

import java.util.List;

public interface StockRecordRepository extends CrudRepository<StockRecord, Long> {
    List<StockRecord> findBySymbol(String symbol);
}
