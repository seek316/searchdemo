package com.example.searchdemo.search.domain;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@Entity
public class SearchKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String keyword;
}
