package com.salpreh.products.restapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${mockserver.host}")
    private String mockServerHost;

    @Value("${mockserver.port}")
    private String mockServerPort;

    @Bean
    public WebClient webClient(WebClient.Builder builder) {

      return builder.baseUrl(
          String.format(
            "http://%s:%s",
            mockServerHost,
            mockServerPort
          ))
        .build();
    }
}
