package pl.piomin.services.stocktrader.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.piomin.services.stocktrader.service.StockService;
import pl.piomin.services.stocktrader.service.providers.ProfitService;
import pl.piomin.services.stocktrader.service.providers.TwelveDataService;

@Configuration
public class StockTraderAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = "trader.api.provider", havingValue = "twelvedata")
    StockService twelveDataService() {
        return new TwelveDataService();
    }

    @Bean
    @ConditionalOnProperty(name = "trader.api.provider", havingValue = "profit")
    StockService profitService() {
        return new ProfitService();
    }
}
