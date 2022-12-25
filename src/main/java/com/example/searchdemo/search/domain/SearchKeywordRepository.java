package com.example.searchdemo.search.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SearchKeywordRepository extends JpaRepository<SearchKeyword, Long> {

    @Query(value =
            " SELECT keyword, COUNT(*) cnt FROM SEARCH_KEYWORD GROUP BY keyword ORDER BY COUNT(*) DESC LIMIT :limit "
            , nativeQuery = true
    )
    List<SearchKeywordRank> findGroupBySearchKeywordWithNativeQuery(@Param("limit") Integer limit);
}
