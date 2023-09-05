package com.example.testspring.repository;

import com.example.testspring.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepo extends JpaRepository<Student,Integer> {
    @Query("SELECT s FROM Student s WHERE s.studentCode LIKE :x")
    public Page<Student> searchByStudentCode(@Param("x") String studentCode, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE s.userId = :x")
    public void searchById(@Param("x") int userId);
}
