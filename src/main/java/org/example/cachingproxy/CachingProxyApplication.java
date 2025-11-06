package org.example.cachingproxy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CachingProxyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CachingProxyApplication.class, args);
    }

    @Bean
    public CommandLineRunner testOriginUrl(ProxyConfiguration proxyConfiguration) {
        return args -> {
            System.out.println("===============================================");
            System.out.println(">>> Target Origin URL: " + proxyConfiguration.getOriginUrl());
            System.out.println("===============================================");
        };
    }
}
