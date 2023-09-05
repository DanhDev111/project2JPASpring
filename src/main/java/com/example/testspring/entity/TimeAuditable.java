package com.example.testspring.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class TimeAuditable {
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @CreatedDate //auto gen new date
    @Column(updatable = false) //khi update minh khong cap nhat minh se giu nguyen
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
