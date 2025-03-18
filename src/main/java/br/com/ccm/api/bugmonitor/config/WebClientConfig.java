package br.com.ccm.api.bugmonitor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${notion.token}")
    private String notionToken;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.notion.com/v1")
                .defaultHeader("Notion-Version", "2022-06-28")
                .defaultHeader("Authorization", "Bearer " + notionToken)
                .build();
    }
}
