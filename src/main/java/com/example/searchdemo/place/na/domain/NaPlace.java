package com.example.searchdemo.place.na.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaPlace {
    private String lastBuildDate;
    private long total;
    private long start;
    private long display;
    private List<NaItem> items;
}
