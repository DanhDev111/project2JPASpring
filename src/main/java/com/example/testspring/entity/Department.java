package com.example.testspring.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Data
@EntityListeners(AuditingEntityListener.class)
@Table(name = "department")
@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @CreatedDate //auto gen new date
    @Column(updatable = false) //khi update minh khong cap nhat minh se giu nguyen
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
    //khong bat buoc
    // one department to many users
    // mappedBy la ten thuoc tính manytoone ben entity user
    // Neu Department d thi de la d
    //Nếu không cần lấy danh sách user từ department thì thôi

    //Eager nó là fetch đồng thời
    // Lazy khi dùng cần đến mới select
    @OneToMany(mappedBy = "department",
                fetch = FetchType.LAZY
//                cascade = CascadeType.ALL
                )
    private List<User> users;
}
