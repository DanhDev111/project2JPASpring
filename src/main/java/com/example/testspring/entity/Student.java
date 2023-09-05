package com.example.testspring.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Student {
    @Id
    private int userId;// nó chính là user_id

    //dung chinh cot id khóa chính  lam id de join luon
    @OneToOne(cascade = CascadeType.ALL,
            fetch =FetchType.EAGER )
    @PrimaryKeyJoinColumn
    @MapsId //copy id user set cho id cua student
    private User user; //nó phải trung với user_id

//    @ManyToMany
//    @JoinTable(name = "score",
//            joinColumns = @JoinColumn(name = "student_id"),
//            inverseJoinColumns = @JoinColumn(name = "course_id")
//    )
//    private List<Course> courses;
    private String studentCode;
//    @OneToMany(mappedBy = "student")
//    private List<Score> scores;


}
