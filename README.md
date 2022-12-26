# 테스트 방법
## 1) 장소 검색
### HTTP Method: GET

    curl http://localhost:8080/api/v1/place?query=우리은행
    curl http://localhost:8080/api/v1/place?query=우리은행본점
    curl http://localhost:8080/api/v1/place?query=하나은행
    curl http://localhost:8080/api/v1/place?query=SC제일은행
    curl http://localhost:8080/api/v1/place?query=IBK기업은행
    curl http://localhost:8080/api/v1/place?query=국민은행
    curl http://localhost:8080/api/v1/place?query=새마을금고

## 2) 검색 키워드 목록
### HTTP Method: GET

    curl http://localhost:8080/api/v1/place/keyword-rank

------------
## 외부 라이브러리에 대해 사용 목적과 선택 사유
* spring-boot-starter-web: 스프링 웹 라이블러리를 사용하기 위함
* spring-boot-starter-data-jpa: 스프링 DB ORM JPA를 사용하기 위함
* spring-boot-starter-webflux: 외부 API를 호출하기 위함
* com.h2database: 임시 DB를 사용하기 위함
* org.projectlombok: Entity 및 Dto 코드 작성 간소화를 위함# searchdemo
