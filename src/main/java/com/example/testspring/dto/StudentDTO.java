package com.example.testspring.dto;

import com.example.testspring.entity.User;
import lombok.Data;

import javax.persistence.*;

@Data
public class StudentDTO {
//    private int userId;

    private UserDTO user;//user_id

    private String studentCode;
    //open trang mo tả documents api có trong hệ thống của mình
}
