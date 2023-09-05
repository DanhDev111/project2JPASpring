package com.example.testspring.controller;

import com.example.testspring.dto.*;
import com.example.testspring.services.DepartmentServices;
import com.example.testspring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.example.testspring.entity.User;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;


    @GetMapping("/list")
    public ResponseDTO<List<UserDTO>> list() {
        List<UserDTO> usersDTO = userService.getAll();
        return ResponseDTO.<List<UserDTO>>builder().status(200).data(usersDTO).build();
    }

    @PostMapping("/search")// copy lai userlist
    //require = false tuc la khong bat buoc cung duoc
    public ResponseDTO<PageDTO<List<UserDTO>>> search(@ModelAttribute SearchDTO searchDTO) {
        //TODO : KHi reqest quá nhiều param
        // thì tạo 1 class mới mình request cho nhanh
        PageDTO<List<UserDTO>> pageUser =
                userService.searchName(searchDTO);
        return ResponseDTO.<PageDTO<List<UserDTO>>>builder().status(200).data(pageUser).build();
    }

    @GetMapping("/download")
    public void download(
            @RequestParam("fileName") String fileName,
            HttpServletResponse response
    ) throws IllegalStateException, IOException {
        File file = new File("F:/" + fileName);
        Files.copy(file.toPath(), response.getOutputStream());
    }

    @PostMapping(value = "/")
//    Map như thế này khi mà tên thuộc tính của user trùng với name trong form
    public ResponseDTO<Void> newUser(
            @ModelAttribute @Valid UserDTO userDTO
    ) throws IOException {
        if (!userDTO.getFile().isEmpty()) {
            String filename = userDTO.getFile().getOriginalFilename();
            // Luu lai file vào ở cùng máy chủ
            File saveFile = new File("F:/" + filename);
            userDTO.getFile().transferTo(saveFile);
            //lay ten file luu xuong DATABASE
            userDTO.setAvatarURL(filename);

        }
        userService.create(userDTO);
        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    @DeleteMapping("/")
    public ResponseDTO<Void> delete(@RequestParam("id") int id) {
        userService.delete(id);
        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    @GetMapping("/")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseDTO<UserDTO> get(@RequestParam("id") int id) {
        //.data chính là đọc từ db lên
        return ResponseDTO.<UserDTO>builder()
                .status(200)
                .data(userService.getById(id))
                .build();
    }

    @PutMapping(value = "/")
    public ResponseDTO<UserDTO> update(@ModelAttribute @Valid UserDTO userDTO) throws IOException {
        if (!userDTO.getFile().isEmpty()) {
            String filename = userDTO.getFile().getOriginalFilename();
            // Luu lai file vào ở cùng máy chủ
            File saveFile = new File("F:/" + filename);
            userDTO.getFile().transferTo(saveFile);
            //lay ten file luu xuong DATABASE
            userDTO.setAvatarURL(filename);
        }
        userService.update(userDTO);
        return ResponseDTO.<UserDTO>builder()
                .status(200).
                data(userDTO).
                build();
    }

}
