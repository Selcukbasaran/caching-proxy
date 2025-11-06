package org.example.cachingproxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.cachingproxy.model.ResponseCache;
import org.example.cachingproxy.repository.ResponseCacheRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ProxyController {
    private final WebClient webClient;
    private final ResponseCacheRepository responseCacheRepository;
    private final ObjectMapper objectMapper;

    @RequestMapping("/**")
    public ResponseEntity<String> proxyRequests(HttpServletRequest request){
        String url = request.getRequestURI();
        String query = request.getQueryString();

        String fullUrl = url + (query != null ? "?" + query : "");
        System.out.println("Proxying request to: " + fullUrl);
        Optional<ResponseCache> cachedResponse = responseCacheRepository.findById(fullUrl);
        if (cachedResponse.isPresent()) {
            String responseHeaders = cachedResponse.get().getResponseHeaders();
            HttpHeaders headers = new HttpHeaders();
            try {
                headers = objectMapper.readValue(responseHeaders, HttpHeaders.class);
            }catch (JsonProcessingException e){
                headers = new HttpHeaders();
            }
            headers.add("X-Cache", "HIT");
            return ResponseEntity
                    .status(cachedResponse.get().getStatusCode())
                    .headers(headers)
                    .body(cachedResponse.get().getResponseBody());
        }
        ResponseCache responseCache = new ResponseCache();
        ResponseEntity<String> response = this.webClient
                .method(HttpMethod.GET)
                .uri(fullUrl)
                .retrieve()
                .toEntity(String.class).block();
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        responseCache.setPath(fullUrl);
        responseCache.setResponseBody(response.getBody());
        try {
            String headersJson = objectMapper.writeValueAsString(response.getHeaders());
            responseCache.setResponseHeaders(headersJson);
        } catch (JsonProcessingException e) {
            responseCache.setResponseHeaders("{}");
        }
        responseCache.setStatusCode(response.getStatusCode().value());
        responseCacheRepository.save(responseCache);

        System.out.println("Response body: " + response.getStatusCode());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.putAll(response.getHeaders());
        responseHeaders.add("X-Cache", "MISS");
        return ResponseEntity
                .status(response.getStatusCode())
                .headers(responseHeaders)
                .body(response.getBody());
    }
}
