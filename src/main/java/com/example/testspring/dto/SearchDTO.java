package com.example.testspring.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SearchDTO{
//    @NotBlank(message = "{not.blank}")
//    @Size(min=2, max=30, message = "{size.msg}")
    private String keyword;
    private Integer currentPage;
    private Integer size;
    private String sortedField;

    public SearchDTO() {
        currentPage =0;
        size = 5;
    }
}
