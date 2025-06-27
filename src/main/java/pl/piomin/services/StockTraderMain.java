package pl.piomin.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.piomin.services.stocktrader.config.ProfitApiProperties;

@SpringBootApplication
@EnableConfigurationProperties(ProfitApiProperties.class)
public class StockTraderMain {
    public static void main(String[] args) {
        SpringApplication.run(StockTraderMain.class, args);
    }
}