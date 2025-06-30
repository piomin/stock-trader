package pl.piomin.services.stocktrader.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.piomin.services.stocktrader.service.providers.StockService;
import pl.piomin.services.stocktrader.service.providers.ProfitService;
import pl.piomin.services.stocktrader.service.providers.TwelveDataService;

@Configuration
public class StockTraderAutoConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(StockTraderAutoConfiguration.class);

    @Bean
    TwelveDataApiProperties twelveDataApiProperties() {
        return new TwelveDataApiProperties();
    }

    @Bean
    @ConditionalOnProperty(name = "trader.api.provider", havingValue = "twelvedata")
    StockService twelveDataService() {
        LOG.info("Using TwelveData API");
        return new TwelveDataService(twelveDataApiProperties());
    }

    @Bean
    ProfitApiProperties profitApiProperties() {
        return new ProfitApiProperties();
    }

    @Bean
    @ConditionalOnProperty(name = "trader.api.provider", havingValue = "profit")
    StockService profitService() {
        LOG.info("Using Profit API");
        return new ProfitService(profitApiProperties());
    }
}
