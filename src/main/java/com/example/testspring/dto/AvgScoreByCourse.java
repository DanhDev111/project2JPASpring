package com.example.testspring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvgScoreByCourse {
    // Để ý 3 thằng thuộc tính sẽ ứng với select 3 thằng đầu tiên trong query
    private int id;
    private String name;
    private double avg;
}
