package com.example.testspring.services;

import com.example.testspring.dto.*;
import com.example.testspring.entity.Course;
import com.example.testspring.entity.Score;
import com.example.testspring.entity.Student;
import com.example.testspring.repository.CourseRepo;
import com.example.testspring.repository.ScoreRepo;
import com.example.testspring.repository.StudentRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface ScoreServices {
    void create(ScoreDTO scoreDTO);
    void update(ScoreDTO scoreDTO);
    void delete(int id);
    ScoreDTO getById(int id);
    List<ScoreDTO> readAll();
    PageDTO<List<ScoreDTO>> search(SearchScoreDTO searchDTO);


    List<AvgScoreByCourse> avgScoreByCourses();
}
@Service
class ScoreServicesImpl implements ScoreServices{
    @Autowired
    ScoreRepo scoreRepo;

    @Autowired
    CourseRepo courseRepo;
    @Autowired
    StudentRepo studentRepo;
    @Override
    @Transactional
    public void create(ScoreDTO scoreDTO) {
        Score score = new ModelMapper().map(scoreDTO,Score.class);

        scoreRepo.save(score);
    }

    @Override
    @Transactional
    public void update(ScoreDTO scoreDTO) {
        Score score = scoreRepo.findById(scoreDTO.getId()).orElse(null);
        score.setScore(scoreDTO.getScore());

        Student student = studentRepo.findById(scoreDTO.getStudent().getUser().getId()).orElseThrow(NoResultException::new);
        score.setStudent(student);

        Course course = courseRepo.findById(scoreDTO.getCourse().getId()).orElseThrow(NoResultException::new);
        score.setCourse(course);

        scoreRepo.save(score);
    }

    @Override
    @Transactional
    public void delete(int id) {
        scoreRepo.deleteById(id);
    }

    @Override
    @Transactional
    public ScoreDTO getById(int id) {
        Score score = scoreRepo.findById(id).orElseThrow(NoResultException::new);
        return convert(score);
    }
    public ScoreDTO convert(Score score){
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper.map(score,ScoreDTO.class);
    }

    @Override
    public List<ScoreDTO> readAll() {
        List<Score> scores = scoreRepo.findAll();
        List<ScoreDTO> scoreDTOList = new ArrayList<>();
        for (Score score : scores){
            scoreDTOList.add(convert(score));
        }
        return scoreDTOList;
    }


    @Override
    public PageDTO<List<ScoreDTO>> search(SearchScoreDTO searchDTO) {
        Sort sort = Sort.by("id").ascending();
        if (StringUtils.hasText(searchDTO.getSortedField())){
            sort = Sort.by(searchDTO.getSortedField()).descending();
        }
        if (searchDTO.getCurrentPage()==null){
            searchDTO.setCurrentPage(0);
        }
        if (searchDTO.getSize()==null){
            searchDTO.setSize(5);
        }
        if (searchDTO.getKeyword()==null){
            searchDTO.setKeyword("");
        }
        PageRequest pageRequest = PageRequest.of(searchDTO.getCurrentPage(),searchDTO.getSize(),sort);
        Page<Score> page = null;
        if (searchDTO.getCourseId()!=null){
            page = scoreRepo.searchByCourse(searchDTO.getCourseId(),pageRequest);
        }else if(searchDTO.getStudentId()!=null){
            page = scoreRepo.searchByStudent(searchDTO.getStudentId(),pageRequest);
        }else{
            page = scoreRepo.findAll(pageRequest);
        }

        PageDTO<List<ScoreDTO>> pageDTO = new PageDTO<>();
        pageDTO.setTotalPages(page.getTotalPages());
        pageDTO.setTotalElements(page.getTotalElements());

        List<ScoreDTO> scoreDTOS = page.get().map(u -> convert(u)).collect(Collectors.toList());
        pageDTO.setData(scoreDTOS);
        return pageDTO;
    }

    @Override
    public List<AvgScoreByCourse> avgScoreByCourses() {
        return scoreRepo.avgScoreByCourse();
    }
}
