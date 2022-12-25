package com.example.searchdemo.place.ka.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KaMeta {
    private Boolean isEnd;
    private Integer pageableCount;
    private KaRegionInfo sameName;
    private Integer totalCount;
}
