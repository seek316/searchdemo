# 테스트 방법
## 1) 장소 검색
### 동일 업체 판단 기준: 업체명 공백 제거 비교
### HTTP Method: GET

```
    curl http://localhost:8080/api/v1/place?query=우리은행
    curl http://localhost:8080/api/v1/place?query=우리은행본점
    curl http://localhost:8080/api/v1/place?query=우리은행본점
    curl http://localhost:8080/api/v1/place?query=하나은행
    curl http://localhost:8080/api/v1/place?query=하나은행
    curl http://localhost:8080/api/v1/place?query=하나은행
    curl http://localhost:8080/api/v1/place?query=SC제일은행
    curl http://localhost:8080/api/v1/place?query=SC제일은행
    curl http://localhost:8080/api/v1/place?query=SC제일은행
    curl http://localhost:8080/api/v1/place?query=SC제일은행
    curl http://localhost:8080/api/v1/place?query=IBK기업은행
    curl http://localhost:8080/api/v1/place?query=IBK기업은행
    curl http://localhost:8080/api/v1/place?query=IBK기업은행
    curl http://localhost:8080/api/v1/place?query=IBK기업은행
    curl http://localhost:8080/api/v1/place?query=IBK기업은행
    curl http://localhost:8080/api/v1/place?query=국민은행
    curl http://localhost:8080/api/v1/place?query=국민은행
    curl http://localhost:8080/api/v1/place?query=국민은행
    curl http://localhost:8080/api/v1/place?query=국민은행
    curl http://localhost:8080/api/v1/place?query=국민은행
    curl http://localhost:8080/api/v1/place?query=국민은행
    curl http://localhost:8080/api/v1/place?query=새마을금고
    curl http://localhost:8080/api/v1/place?query=새마을금고
    curl http://localhost:8080/api/v1/place?query=새마을금고
    curl http://localhost:8080/api/v1/place?query=새마을금고
    curl http://localhost:8080/api/v1/place?query=새마을금고
    curl http://localhost:8080/api/v1/place?query=새마을금고
    curl http://localhost:8080/api/v1/place?query=GS25
    curl http://localhost:8080/api/v1/place?query=GS25
    curl http://localhost:8080/api/v1/place?query=CU
    curl http://localhost:8080/api/v1/place?query=CU
    curl http://localhost:8080/api/v1/place?query=세븐일레븐
    curl http://localhost:8080/api/v1/place?query=세븐일레븐
    curl http://localhost:8080/api/v1/place?query=이마트
```

## 2) 검색 키워드 목록
### HTTP Method: GET

```
    curl http://localhost:8080/api/v1/place/keyword-rank
```

------------
## 외부 라이브러리에 대해 사용 목적과 선택 사유
* spring-boot-starter-web: 스프링 웹 라이블러리를 사용하기 위함
* spring-boot-starter-data-jpa: DB ORM JPA를 사용하기 위함
* spring-boot-starter-webflux: API 호출 라이브러리 를 사용하기 위함
* com.h2database: 임시 DB를 사용하기 위함
* org.projectlombok: domain 코드 작성 간소화를 위함

------------
## 기술적 요구사항
* 동시성 이슈가 발생할 수 있는 부분을 염두에 둔 설계 및 구현 (예시. 키워드 별로 검색된 횟수)
```
    // 검색 키워드 데이터가 누적되도록 작업 (update 없이 insert 만하기 때문에 동시성 이슈에 안전함)
    @Transactional
    public SearchKeyword createSearchKeyword(String query) {
        return searchKeywordRepository.save(SearchKeyword.builder().keyword(query).build());
    }

    // 아래와 같은 쿼리로 검색 키워드 순위 조회
    SELECT keyword, COUNT(*) cnt FROM SEARCH_KEYWORD GROUP BY keyword ORDER BY COUNT(*) DESC LIMIT 10

```

* 카카오, 네이버 등 검색 API 제공자의 “다양한” 장애 발생 상황에 대한 고려
```
    // 아래와 같이 API 호출 시 예외가 발생하면 에러 로그를 남기고 이후 프로세스 계속 진행되도록함.
    public KaResponse findPlaceListByQuery(String query) {
        KaResponse result;
        try {
            result = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/local/search/keyword.json")
                            .queryParam("query", query)
                            .queryParam("size", 5)
                            .build())
                    .retrieve()
                    .bodyToMono(KaResponse.class)
                    .block();
        } catch (Exception e) {
            log.error(e.getMessage());
            result = null;
        }
        return result;
    }
```

* 구글 장소 검색 등 새로운 검색 API 제공자의 추가 시 변경 영역 최소화에 대한 고려
```
    public List<Place> findPlaceListByQuery(String query) {
        List<Place> resultList = new ArrayList<>();
        int subOrder = 1;

        // 카카오
        KaResponse kaResponse = kaSearchService.findPlaceListByQuery(query);
        if (kaResponse != null) {
            for (KaPlace kaPlace : kaResponse.getDocuments()) {
                resultList.add(...);
            }
        }

        // 네이버
        NaPlace naPlace = naSearchService.findPlaceListByQuery(query);
        if (naPlace != null) {
            for (NaItem item : naPlace.getItems()) {
                resultList.add(...);
            }
        }

        // Todo 이부분이 새로운 검색 API 제공자 추가되는 영역이다.

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
```

* 지속적 유지 보수 및 확장에 용이한 아키텍처에 대한 설계
    * API 제공자 별 package 구조 분리 (ka: 카카오, na: 네이버, ...)
    * ![image](https://user-images.githubusercontent.com/121388755/209666785-4852e85e-4f85-4df1-b9b7-5f31470fb771.png)


