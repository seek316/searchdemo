package com.example.searchdemo.place.ka.service;

import com.example.searchdemo.place.ka.domain.KaResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KaSearchService {

    public static final String HOST = "https://dapi.kakao.com";
    public static final String API_KEY = "KakaoAK 8a7030ec71ff020c94296f98b0fdc103";
    private final WebClient webClient;

    public KaSearchService() {
        webClient = WebClient.builder()
                .baseUrl(HOST)
                .defaultHeader(HttpHeaders.AUTHORIZATION, API_KEY)
                .build();
    }

    public Mono<KaResponseEntity> findPlaceListByQuery(String query) {
        // 1.요청
        Mono<KaResponseEntity> flux = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/keyword.json")
                        .queryParam("query", query)
                        .queryParam("size", 5)
                        .build())
                .retrieve()
                .bodyToMono(KaResponseEntity.class);
        return flux;
    }
}
