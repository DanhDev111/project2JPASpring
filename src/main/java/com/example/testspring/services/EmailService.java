package com.example.testspring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class EmailService {
    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    SpringTemplateEngine templateEngine;

    public void sendBirthdayEmail(String to,
            String name){

        String subject = "Happy BirthDay!"+name;
        Context ctx = new Context(); //Thư viện của themeleaf
        ctx.setVariable("name",name);
        String body = templateEngine.process("emailBirthDay.html",ctx);
        sendEmail(to, subject, body);
    }

    public void sendEmail(String to, String subject, String body) {
        MimeMessage message =  javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body,true);
            helper.setFrom("tducanh99999@gmail.com"); //yourgmail@gmail.com

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void testMailSender(){
        String to = "tducanh99999@gmail.com";
        String subject = "Hi!Đanh";
        String body = "<h1>Nội dung:Chúc mừng bạn đã hoàn thaành xuất sắc khóa học</h1>";
        sendEmail(to, subject, body);
    }
}
