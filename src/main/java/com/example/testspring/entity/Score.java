package com.example.testspring.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "score")
@EqualsAndHashCode(callSuper = true)
public class Score extends TimeAuditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double score;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Course course;


}
