package com.example.searchdemo.search.service;

import com.example.searchdemo.place.domain.Place;
import com.example.searchdemo.search.domain.SearchKeyword;
import com.example.searchdemo.search.domain.SearchKeywordRepository;
import com.example.searchdemo.search.domain.SearchKeywordRank;
import com.example.searchdemo.place.ka.domain.KaPlace;
import com.example.searchdemo.place.ka.domain.KaResponseEntity;
import com.example.searchdemo.place.ka.service.KaSearchService;
import com.example.searchdemo.place.na.domain.NaItem;
import com.example.searchdemo.place.na.domain.NaPlace;
import com.example.searchdemo.place.na.service.NaSearchService;
import com.example.searchdemo.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchPlaceService {

    public static final String EMPTY = "";
    public static final String SPACE = " ";
    public static final String REMOVE_HTML = "<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>";

    private final KaSearchService kaSearchService;
    private final NaSearchService naSearchService;
    private final SearchKeywordRepository searchKeywordRepository;

    public List<Place> findPlaceListByQuery(@RequestParam String query) {
        List<Place> resultList = new ArrayList<>();
        int subOrder = 1;

        // 카카오
        Mono<KaResponseEntity> ka = kaSearchService.findPlaceListByQuery(query);
        for (KaPlace kaPlace : ka.block().getDocuments()) {
            resultList.add(
                    Place.builder()
                            .placeName(kaPlace.getPlaceName().replaceAll(SPACE, EMPTY).replaceAll(REMOVE_HTML, EMPTY))
                            .roadAddress(kaPlace.getRoadAddressName())
                            .subOrder(subOrder++)
                            .build()
            );
        }

        // 네이버
        Mono<NaPlace> na = naSearchService.findPlaceListByQuery(query);
        for (NaItem item : na.block().getItems()) {
            resultList.add(
                    Place.builder()
                            .placeName(item.getTitle().replaceAll(SPACE, EMPTY).replaceAll(REMOVE_HTML, EMPTY))
                            .roadAddress(item.getRoadAddress())
                            .subOrder(subOrder++)
                            .build()
            );
        }

        for (Place place : resultList) {
            if (CommonUtil.isDuplicated(resultList, place.getPlaceName())) place.setMainOrder(1);
            else place.setMainOrder(2);
        }

        return CommonUtil.deduplication(
                resultList.stream()
                        .sorted(Comparator.comparing(Place::getMainOrder).thenComparing(Place::getSubOrder))
                        .collect(Collectors.toList())
                , Place::getPlaceName);
    }

    public SearchKeyword createSearchKeyword(String query) {
        return searchKeywordRepository.save(SearchKeyword.builder().keyword(query).build());
    }

    public List<SearchKeywordRank> findSearchKeywordRank(Integer limit) {
        return searchKeywordRepository.findGroupBySearchKeywordWithNativeQuery(limit);
    }


}