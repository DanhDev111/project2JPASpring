package com.example.testspring.repository;

import com.example.testspring.dto.AvgScoreByCourse;
import com.example.testspring.entity.Course;
import com.example.testspring.entity.Score;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScoreRepo extends JpaRepository<Score,Integer> {
    //Mỗi object sẽ tương ứng với 1 cột ở đây chúng ta có 3 cột(line 30 )

    @Query("SELECT s FROM Score s WHERE s.student.userId = :sid ")
    Page<Score> searchByStudent(@Param("sid") int studentId, Pageable pageable);

    @Query("SELECT s FROM Score s WHERE s.course.id = :cid ")
    Page<Score> searchByCourse(@Param("cid") int courseId, Pageable pageable);

    //Thống kê trung bình cộng điểm theo 1 môn học
    @Query("SELECT new com.example.testspring.dto.AvgScoreByCourse("
            +"c.id,c.name, AVG(s.score)" +
            ")"
            +" FROM Score s JOIN s.course c GROUP BY c.id,c.name ")
    List<AvgScoreByCourse> avgScoreByCourse();

    @Query("SELECT c.id,c.name, AVG(s.score) "
            +" FROM Score s JOIN s.course c GROUP BY c.id,c.name ")
    List<Object[]> avgScoreByCourse2();

}
