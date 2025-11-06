package org.example.cachingproxy;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Getter
@Configuration
public class ProxyConfiguration {

    @Value("${origin:http://dummyjson.com}")
    private String originUrl;

    @Bean
    public WebClient webClient(){
        return WebClient.builder().baseUrl(this.originUrl).build();
    }

}
