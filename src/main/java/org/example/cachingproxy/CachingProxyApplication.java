package org.example.cachingproxy;

import org.example.cachingproxy.repository.ResponseCacheRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

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

    @Bean
    public CommandLineRunner clearCache(ResponseCacheRepository responseCacheRepository, ApplicationContext appContext) {
        return args -> {
            boolean clear = Arrays.asList(args).contains("--clear-cache");
            if (clear) {
                responseCacheRepository.deleteAll();
                System.out.println("Cache cleared.");
                int exitCode = SpringApplication.exit(appContext, () -> 0);
                System.exit(exitCode);
            }
        };
    }
}
