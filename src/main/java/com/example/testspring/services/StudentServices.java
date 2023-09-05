package com.example.testspring.services;

import com.example.testspring.dto.DepartmentDTO;
import com.example.testspring.dto.PageDTO;
import com.example.testspring.dto.SearchDTO;
import com.example.testspring.dto.StudentDTO;
import com.example.testspring.entity.Department;
import com.example.testspring.entity.Student;
import com.example.testspring.entity.User;
import com.example.testspring.repository.StudentRepo;
import com.example.testspring.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

public interface StudentServices {

    void create(StudentDTO studentDTO);

    void update(StudentDTO studentDTO);

    void delete(int id);

    StudentDTO getById(int id);

    List<StudentDTO> readAll();

    PageDTO<List<StudentDTO>> search(SearchDTO searchDTO);
}

@Service
class StudentServicesImpl implements StudentServices {

    @Autowired
    UserRepo userRepo;

    @Autowired
    StudentRepo studentRepo;

    @Override
    @Transactional
    public void create(StudentDTO studentDTO) {
//        User user = new ModelMapper().map(studentDTO.getUser(), User.class);
//        userRepo.save(user);

        //dung casecade
        Student student = new ModelMapper().map(studentDTO, Student.class);
//        student.setUser(user);
//        student.setStudentCode(studentDTO.getStudentCode());

        studentRepo.save(student);
    }

    @Override
    @Transactional
    public void update(StudentDTO studentDTO) {
        Student currentStudent = studentRepo.findById(studentDTO.getUser().getId()).orElse(null);
        if (currentStudent != null) {
            currentStudent.setStudentCode(studentDTO.getStudentCode());
            studentRepo.save(currentStudent);
        }
    }

    @Override
    @Transactional
    public void delete(int id) {
        studentRepo.deleteById(id);
    }

    @Override
    @Transactional
    public StudentDTO getById(int id) {
        Student student = studentRepo.findById(id).orElseThrow(NoResultException::new);
        return convert(student);
    }

    public StudentDTO convert(Student student) {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return modelMapper.map(student, StudentDTO.class);

    }

    @Override
    @Transactional
    public List<StudentDTO> readAll() {
        return null;
    }

    @Override
    public PageDTO<List<StudentDTO>> search(SearchDTO searchDTO) {
        Sort sortBy = Sort.by("studentCode").ascending();
        if (StringUtils.hasText(searchDTO.getSortedField())) {
            sortBy = Sort.by(searchDTO.getSortedField()).descending();
        }
        if (searchDTO.getCurrentPage() == null) {
            searchDTO.setCurrentPage(0);
        }
        if (searchDTO.getSize() == null) {
            searchDTO.setSize(5);
        }
        PageRequest pageRequest = PageRequest.of(searchDTO.getCurrentPage(), searchDTO.getSize(), sortBy);
        Page<Student> page = studentRepo.searchByStudentCode("%" + searchDTO.getKeyword() + "%", pageRequest);

        PageDTO<List<StudentDTO>> pageDTO = new PageDTO<>();
        pageDTO.setTotalPages(page.getTotalPages());
        pageDTO.setTotalElements(page.getTotalElements());

        List<StudentDTO> studentDTOs =
                page.get().map(u -> convert(u)).collect(Collectors.toList());
        pageDTO.setData(studentDTOs);
        return pageDTO;

    }
}
