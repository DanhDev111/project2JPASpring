package com.example.testspring.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAOP {
    //Thay vi chen code service vao cua nguoi khac minh se di dung tinh nang nay
    //Minhf chen vao se khong lam anh huong gi den code cua nguoi ta ca
    @Before("execution(* com.example.testspring.services.DepartmentServices.getById(*))")
    public void getDepartmentById(JoinPoint joinPoint){
        int id = (int) joinPoint.getArgs()[0];
        log.info("JOIN POINT:"+id);
    }
}
