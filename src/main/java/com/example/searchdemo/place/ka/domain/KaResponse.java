package com.example.searchdemo.place.ka.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KaResponse {
    private List<KaPlace> documents;
    private KaMeta meta;
}
