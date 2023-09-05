package com.example.testspring.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {
    //1.Attributes
    //Mình trả về 1 dạng là generic mình trả veef kiểu gì cũng đc
    //   ResponseDTO<T> generic T (type)
    private int status;// error 200, 400, 500
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
    //2.Constructor
    //Nếu dùng builder thì thêm 2 constructor vào
    public ResponseDTO(int status, String msg) {
        super();
        this.status = status;
        this.msg = msg;
    }
}
