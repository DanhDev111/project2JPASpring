package com.example.testspring.dto;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.validation.constraints.NotBlank;
import java.util.Date;
@Data
public class DepartmentDTO {
    //Tách ra nó liên quan đến giao diện việc map giao diện(UI với UX) với form
    private int id;

    @NotBlank
    private String name;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;

    private Date updatedAt;
}
