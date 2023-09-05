package com.example.testspring.controller;

import com.example.testspring.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.NoResultException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionController {

    // log
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler({NoResultException.class})
    public ResponseDTO<String> notFound(NoResultException e) {
        logger.info("INFO", e);

//        return ResponseDTO.<String>builder()
//                .status(404).msg("No Data").build();
        //hoặc là
        ResponseDTO<String> responseDTO = new ResponseDTO<>(400, "No Data!");
//        responseDTO.setStatus(404);
//        responseDTO.setMsg("No data!");
        return responseDTO;
    }

    //Neu minh nhap trung thi se ban ra exception
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ResponseDTO<String> duplicate(Exception e) {
        log.info("INFO", e);
        return ResponseDTO.<String>builder().status(409).msg("DUPLICATED DATA").build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ResponseDTO<String> accessDeny(Exception e){
        log.info("INFO",e);
        return ResponseDTO.<String>builder().status(403).msg("Access Denied").build();
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseDTO<String> badCredential(Exception e){
        log.info("INFO",e);
        return ResponseDTO.<String>builder().status(401).msg(e.getMessage()).build();
    }
    //Thay vì mình trả về ResponseEntity<String> mình sẽ viết trả về ResponseDTO<String>
    @ExceptionHandler({BindException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST) //HTTP STATUS CODE
    public ResponseDTO<String> badRequest(BindException e) {
        //Nên nhớ thằng ResponseDTO<String> là do mình tự tạo ra còn thằng ResponseEntity<String> là của spring nó tạo
        //Chú ý phân biệt
        logger.info("Bad request");
        List<ObjectError> errors = e.getBindingResult().getAllErrors();

        String msg = "";
        for (ObjectError err : errors) {
            FieldError fieldError = (FieldError) err;
            msg += fieldError.getField() + ":" + err.getDefaultMessage() + ";";
        }
        return ResponseDTO.<String>builder()
                .status(400).msg(msg).build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDTO<String> internalServerError(Exception e){
        log.info("INFO",e);
        return ResponseDTO.<String>builder().status(500).msg("ERROR INTERNAL SEVER").build();
    }
}
