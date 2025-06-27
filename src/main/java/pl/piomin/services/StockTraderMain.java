package pl.piomin.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.piomin.services.stocktrader.config.ProfitApiProperties;

@SpringBootApplication
@EnableConfigurationProperties(ProfitApiProperties.class)
@EnableScheduling
public class StockTraderMain {
    public static void main(String[] args) {
        SpringApplication.run(StockTraderMain.class, args);
    }
}