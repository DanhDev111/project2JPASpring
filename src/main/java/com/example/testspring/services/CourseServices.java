package com.example.testspring.services;

import com.example.testspring.dto.CourseDTO;
import com.example.testspring.dto.PageDTO;
import com.example.testspring.dto.SearchDTO;
import com.example.testspring.entity.Course;
import com.example.testspring.repository.CourseRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface CourseServices {

    void create(CourseDTO courseDTO);

    void update(CourseDTO courseDTO);
    void delete(int id);

    CourseDTO getById(int id);

    List<CourseDTO> readAll();

    PageDTO<List<CourseDTO>> search(SearchDTO searchDTO);
}
@Service
class CourseServicesImpl implements CourseServices{
    @Autowired
    CourseRepo courseRepo;
    @Override
    @Transactional
    public void create(CourseDTO courseDTO) {
        Course course = new ModelMapper().map(courseDTO,Course.class);
        courseRepo.save(course);
    }

    @Override
    @Transactional
    public void update(CourseDTO courseDTO) {
        Course currentCourse = courseRepo.findById(courseDTO.getId()).orElse(null);
        if (currentCourse !=null){
            currentCourse.setName(courseDTO.getName());
            courseRepo.save(currentCourse);
        }
    }

    @Override
    @Transactional
    public void delete(int id) {
        courseRepo.deleteById(id);
    }

    @Override
    public CourseDTO getById(int id) {
        Course course = courseRepo.findById(id).orElseThrow(NoResultException::new);
        return convert(course);
    }

    public CourseDTO convert(Course course){
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper.map(course,CourseDTO.class);
    }
    @Override
    public List<CourseDTO> readAll() {
        List<Course> courses = courseRepo.findAll();
        List<CourseDTO> courseDTOList = new ArrayList<>();
        for (Course course: courses){
            courseDTOList.add(convert(course));
        }
        return courseDTOList;
    }

    @Override
    public PageDTO<List<CourseDTO>> search(SearchDTO searchDTO) {
        Sort sort = Sort.by("name").ascending();
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
        Page<Course> page = courseRepo.searchByName("%"+searchDTO.getKeyword()+"%",pageRequest);

        PageDTO<List<CourseDTO>> pageDTO = new PageDTO<>();
        pageDTO.setTotalPages(page.getTotalPages());
        pageDTO.setTotalElements(page.getTotalElements());

        List<CourseDTO> courseDTOS = page.get().map(u -> convert(u)).collect(Collectors.toList());
        pageDTO.setData(courseDTOS);
        return pageDTO;
    }
}
