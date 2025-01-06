package com.capstone.shop.user.v1.service;

import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandiseTitlePredictRequest;
import com.capstone.shop.user.v1.controller.dto.merchandise.UserWebMerchandiseTitlePredictResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class FastApiService {

    private final WebClient webClient;

    public FastApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://api.induk.shop:8000").build();
    }

    public String getPrediction(String title) {
        try {
            // FastAPI 호출 (쿼리 파라미터로 전달)
            Mono<Map> responseMono = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/predict")
                            .queryParam("title", title) // 쿼리 파라미터 추가
                            .build())
                    .retrieve()
                    .bodyToMono(Map.class);

            Map<String, String> response = responseMono.block();
            return response != null ? response.get("prediction") : "Unknown";
        } catch (WebClientResponseException e) {
            throw new RuntimeException("FastAPI 호출 실패: " + e.getStatusCode() + " " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("FastAPI 호출 실패: " + e.getMessage(), e);
        }
    }
}

