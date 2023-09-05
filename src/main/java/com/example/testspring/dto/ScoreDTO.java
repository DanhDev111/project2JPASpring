package com.example.testspring.dto;

import com.example.testspring.entity.Course;
import com.example.testspring.entity.Student;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.ManyToOne;
import java.util.Date;

@Data
public class ScoreDTO {
    private int id;

    private double score;

    private StudentDTO student;

    private CourseDTO course;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm",timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm",timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;
}
