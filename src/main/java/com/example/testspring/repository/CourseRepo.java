package com.example.testspring.repository;

import com.example.testspring.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepo extends JpaRepository<Course,Integer> {
    //Viet lenh sql lam thong ke
    @Query("SELECT c FROM Course c WHERE c.name LIKE :x ")
    Page<Course> searchByName(@Param("x") String name, Pageable pageable);
}
