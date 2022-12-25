package com.example.searchdemo.search.controller;

import com.example.searchdemo.place.domain.Place;
import com.example.searchdemo.search.domain.SearchKeywordRank;
import com.example.searchdemo.search.service.SearchPlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/place")
@RequiredArgsConstructor
public class SearchPlaceController {

    private final SearchPlaceService searchPlaceService;

    @GetMapping
    public ResponseEntity<List<Place>> findPlaceList(@RequestParam String query) {
        searchPlaceService.createSearchKeyword(query);
        return new ResponseEntity<>(searchPlaceService.findPlaceListByQuery(query), HttpStatus.OK);
    }

    @GetMapping("/keyword-rank")
    public ResponseEntity<List<SearchKeywordRank>> findSearchKeywordRank(@RequestParam(defaultValue = "10") Integer limit) {
        return new ResponseEntity<>(searchPlaceService.findSearchKeywordRank(limit), HttpStatus.OK);
    }

}
