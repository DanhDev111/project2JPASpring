package com.example.testspring.controller;

import com.example.testspring.dto.ResponseDTO;
import com.example.testspring.entity.User;
import com.example.testspring.repository.UserRepo;
import com.example.testspring.services.JwTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class LoginController {
    @Autowired
    UserRepo userRepo;

    @Autowired
    JwTokenService jwTokenService;
    @Autowired
    AuthenticationManager authenticationManager;
    @PostMapping("/login")
    public ResponseDTO<String> login(HttpSession session,
                                    @RequestParam("username") String username,
                                    @RequestParam("password") String password){
        //authen
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        // if login success,jwt gen token string   else throw exception above
       return ResponseDTO.<String>builder()
               .status(200)
               .data(jwTokenService.createToken(username))
               .msg("OK").build();
    }
}
