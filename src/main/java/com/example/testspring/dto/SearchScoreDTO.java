package com.example.testspring.dto;

import com.example.testspring.entity.Course;
import com.example.testspring.entity.Student;
import lombok.Data;

@Data
public class SearchScoreDTO extends SearchDTO{
    private Integer studentId;
    private Integer courseId;
}
