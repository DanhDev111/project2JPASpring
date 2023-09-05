package com.example.testspring.controller;

import com.example.testspring.dto.*;
import com.example.testspring.repository.CourseRepo;
import com.example.testspring.services.CourseServices;
import com.example.testspring.services.ScoreServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/score")
public class ScoreController {
    @Autowired
    ScoreServices scoreService;
    @Autowired
    CourseRepo courseRepo;

    @PostMapping("/search")
    public ResponseDTO<PageDTO<List<ScoreDTO>>> search(
            @RequestBody @Valid SearchScoreDTO searchDTO){
//        List<ScoreDTO> scoreDTOS = scoreService.readAll();
        PageDTO<List<ScoreDTO>> pageDTO = scoreService.search(searchDTO);
        return ResponseDTO.<PageDTO<List<ScoreDTO>>>builder().status(200).data(pageDTO).build();
    }
    @PostMapping("/")
    public ResponseDTO<Void> create(@RequestBody ScoreDTO scoreDTO){
        scoreService.create(scoreDTO);
        return ResponseDTO.<Void>builder().status(200).build();
    }
    @GetMapping("/")
    public ResponseDTO<ScoreDTO> getById(@RequestParam("id") int id){

        return ResponseDTO.<ScoreDTO>builder()
                .status(200)
                .data(scoreService.getById(id)).build();
    }
    @DeleteMapping("/")
    public ResponseDTO<Void> delete(@RequestParam("id") int id){
        scoreService.delete(id);
        return ResponseDTO.<Void>builder().status(200).build();
    }
    @PutMapping ("/")
    public ResponseDTO<ScoreDTO> update(@RequestBody @Valid ScoreDTO scoreDTO){
        scoreService.update(scoreDTO);
        return ResponseDTO.<ScoreDTO>builder()
                .status(200)
                .data(scoreDTO).build();
    }
    @GetMapping("/avg-score-by-course")
    public ResponseDTO<List<AvgScoreByCourse>> avgScoreByCourse(){

        return ResponseDTO.<List<AvgScoreByCourse>>builder()
                .status(200)
                .data(scoreService.avgScoreByCourses())
                .build();
    }
}
