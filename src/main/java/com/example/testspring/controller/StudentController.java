package com.example.testspring.controller;

import com.example.testspring.dto.PageDTO;
import com.example.testspring.dto.ResponseDTO;
import com.example.testspring.dto.SearchDTO;
import com.example.testspring.dto.StudentDTO;
import com.example.testspring.entity.Student;
import com.example.testspring.services.StudentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    StudentServices studentServices;

    //Gia su khong upload file
    @PostMapping("/")
    public ResponseDTO<Void> create(
            @RequestBody @Valid StudentDTO studentDTO
    ){
        studentServices.create(studentDTO);
        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }
    @PostMapping("/search")
    public ResponseDTO<PageDTO<List<StudentDTO>>> search(
            SearchDTO searchDTO
    ){
        PageDTO<List<StudentDTO>> pageDTO = studentServices.search(searchDTO);

        return ResponseDTO.<PageDTO<List<StudentDTO>>>builder()
                .status(200)
                .data(pageDTO)
                .build();
    }

    @GetMapping("/")
    public ResponseDTO<StudentDTO> getById(
            @RequestParam("id") int id
    ){
        return ResponseDTO.<StudentDTO>builder()
                .status(200)
                .data(studentServices.getById(id))
                .build();
    }
    @DeleteMapping("/")
    public ResponseDTO<Void> delete(
            @RequestParam("id") int id
    )
    {
        studentServices.delete(id);
        return ResponseDTO.<Void>builder()
                .status(200)
                .msg("OK")
                .build();
    }
    @PutMapping("/")
    public ResponseDTO<StudentDTO> update(
            @RequestBody StudentDTO studentDTO
    ){
        studentServices.update(studentDTO);
        return ResponseDTO.<StudentDTO>builder()
                .status(200)
                .data(studentDTO)
                .build();
    }
}
