package pl.piomin.services.stocktrader.repository;

import org.springframework.data.repository.CrudRepository;
import pl.piomin.services.stocktrader.model.entity.StockTrade;

import java.time.LocalDate;
import java.util.List;

public interface StockTradeRepository extends CrudRepository<StockTrade, Long> {
    StockTrade findBySymbolAndTypeAndDate(String symbol, String type, LocalDate date);
    List<StockTrade> findByTypeOrderByDateDesc(String type);
}
