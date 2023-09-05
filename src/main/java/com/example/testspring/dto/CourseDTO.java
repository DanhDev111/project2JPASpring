package com.example.testspring.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class CourseDTO {
    private int id;
    @NotBlank(message = "Không đc để trống")
    private String name;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm",timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm",timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;
}
