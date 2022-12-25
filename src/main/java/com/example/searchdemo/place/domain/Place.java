package com.example.searchdemo.place.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Place {
    private String placeName;
    private String roadAddress;
    @JsonIgnore
    private Integer mainOrder;
    @JsonIgnore
    private Integer subOrder;
}
