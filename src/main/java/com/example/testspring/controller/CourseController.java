package com.example.testspring.controller;

import com.example.testspring.dto.*;
import com.example.testspring.entity.Course;
import com.example.testspring.repository.CourseRepo;
import com.example.testspring.services.CourseServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    CourseServices courseService;
    @Autowired
    CourseRepo courseRepo;

    @PostMapping("/search")
    public ResponseDTO<PageDTO<List<CourseDTO>>> search(
            @ModelAttribute @Valid SearchDTO searchDTO){
        List<CourseDTO> courseDTOS = courseService.readAll();
        PageDTO<List<CourseDTO>> pageDTO = courseService.search(searchDTO);
        return ResponseDTO.<PageDTO<List<CourseDTO>>>builder().status(200).data(pageDTO).build();
    }
    @PostMapping("/")
    public ResponseDTO<Void> create(@RequestBody @Valid CourseDTO courseDTO){
        courseService.create(courseDTO);
        return ResponseDTO.<Void>builder().status(200).build();
    }
    @GetMapping("/")
    public ResponseDTO<CourseDTO> getById(@RequestParam("id") int id){

        return ResponseDTO.<CourseDTO>builder()
                .status(200)
                .data(courseService.getById(id)).build();
    }
    @DeleteMapping("/")
    public ResponseDTO<Void> delete(@RequestParam("id") int id){
        courseService.delete(id);
        return ResponseDTO.<Void>builder().status(200).build();
    }
    @PutMapping ("/")
    public ResponseDTO<CourseDTO> update(@RequestBody @Valid CourseDTO courseDTO){
        courseService.update(courseDTO);
        return ResponseDTO.<CourseDTO>builder()
                .status(200)
                .data(courseDTO).build();
    }
}
