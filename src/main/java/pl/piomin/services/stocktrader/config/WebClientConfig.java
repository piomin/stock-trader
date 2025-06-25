package pl.piomin.services.stocktrader.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class WebClientConfig {

    @Value("${twelvedata.api.base-url:https://api.twelvedata.com}")
    private String baseUrl;
    
    @Value("${twelvedata.api.key:demo}")
    private String apiKey;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "apikey " + apiKey)
                .build();
    }
}
