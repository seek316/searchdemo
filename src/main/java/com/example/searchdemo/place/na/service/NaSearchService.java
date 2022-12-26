package com.example.searchdemo.place.na.service;

import com.example.searchdemo.place.na.domain.NaPlace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class NaSearchService {

    public static final String HOST = "https://openapi.naver.com";
    public static final String X_NAVER_CLIENT_ID = "X-Naver-Client-Id";
    public static final String X_NAVER_CLIENT_SECRET = "X-Naver-Client-Secret";
    public static final String ID = "1_3wwsg03Pwn4Ax44V3W";
    public static final String SECRET = "a3kB4cUqVL";
    private final WebClient webClient;

    public NaSearchService() {
        webClient = WebClient.builder()
                .baseUrl(HOST)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(X_NAVER_CLIENT_ID, ID);
                    httpHeaders.add(X_NAVER_CLIENT_SECRET, SECRET);
                })
                .build();
    }

    public NaPlace findPlaceListByQuery(String query) {
        NaPlace result;
        try {
            Mono<NaPlace> mono = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1/search/local.json")
                            .queryParam("query", query)
                            .queryParam("display", 5)
                            .build())
                    .retrieve()
                    .bodyToMono(NaPlace.class);
            result = mono.block();
        } catch (Exception e) {
            log.error(e.getMessage());
            result = null;
        }
        return result;
    }
}
