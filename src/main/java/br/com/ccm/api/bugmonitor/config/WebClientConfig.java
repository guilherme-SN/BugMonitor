package br.com.ccm.api.bugmonitor.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${notion.token}")
    private String notionToken;

    @Value("${discord.bot.token}")
    private String botToken;

    @Bean
    public WebClient notionWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.notion.com/v1")
                .defaultHeader("Notion-Version", "2022-06-28")
                .defaultHeader("Authorization", "Bearer " + notionToken)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configure -> configure
                                .defaultCodecs()
                                .maxInMemorySize(5 * 1024 * 1024))
                        .build())
                .build();
    }

    @Bean
    public WebClient discordWebClient() {
        return WebClient.builder()
                .baseUrl("https://discord.com/api/v10")
                .defaultHeader("Authorization", "Bot " + botToken)
                .build();
    }
}
