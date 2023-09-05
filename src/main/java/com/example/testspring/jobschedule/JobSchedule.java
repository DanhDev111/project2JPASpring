package com.example.testspring.jobschedule;

import com.example.testspring.entity.User;
import com.example.testspring.repository.UserRepo;
import com.example.testspring.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.List;

@Component
@Slf4j
public class JobSchedule {
    @Autowired
    UserRepo userRepo;

    @Autowired
    EmailService emailService;

//    @Scheduled(fixedDelay = 60000)
    public void hello(){
        log.info("Hello");
//        emailService.testMailSender();
    }

    // Viết 1 câu lệnh sql tìm danh sách người sinh nhật là ngày hôm nay



    //Rồi for if else
    //Cần gửi vào lúc 9h sáng
    //Thiết kế các câu chúc mưừng buổi sáng
    //Giả sử chat abwngf zalo cũng có api gọi dữ liệu trả về
    //Có thể kết nối với AI chat GPT để tán gái cũng được
//    @Scheduled(cron = "54 12 21 * * *")
    public void goodMorning(){
        Calendar calendar = Calendar.getInstance();
        int date  = calendar.get(Calendar.DATE);
        int month  = calendar.get(Calendar.MONTH) + 1;
        //Tháng 1 tương ứng với 0
        List<User> users = userRepo.searchUserHasBirthDay(date,month);
        for (User user: users){
            log.info("Happy BirthDay!"+ user.getName());
            emailService.sendBirthdayEmail(user.getEmail(), user.getName());
        }

    }
}
