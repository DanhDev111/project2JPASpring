package com.example.testspring.controller;

import com.example.testspring.dto.DepartmentDTO;
import com.example.testspring.dto.PageDTO;
import com.example.testspring.dto.ResponseDTO;
import com.example.testspring.dto.SearchDTO;
import com.example.testspring.entity.Department;
import com.example.testspring.services.DepartmentServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
// webservices REST
@RequestMapping("/department")
@RestController
public class DepartmentController {
    @Autowired
    DepartmentServices departmentServices;
//    @GetMapping("/department/list")
//    public String listDepartment(Model model){
//        List<DepartmentDTO> departmentDTOList = departmentServices.readAll();
//        model.addAttribute("departmentList",departmentDTOList);
//        model.addAttribute("searchDTO",new SearchDTO());
//        return "department/department-list";
//    }

    @PostMapping("/search")//nó sẽ convert về dạng h phảijackson
    public ResponseDTO<PageDTO<List<DepartmentDTO>>> search(
                         @ModelAttribute @Valid SearchDTO searchDTO
                         ){
        // Cách debug
//        System.out.println(searchDTO.toString());
        PageDTO<List<DepartmentDTO>> pageDTO = departmentServices.search(searchDTO);
        return ResponseDTO.<PageDTO<List<DepartmentDTO>>>builder()
                .status(200)
                .data(pageDTO)
                .build();
    }

    @PostMapping("/")
    public ResponseDTO<Void> newDepartment(
            @ModelAttribute @Valid DepartmentDTO departmentDTO
            )
    {
        departmentServices.create(departmentDTO);
//        if (true)
//            return ResponseEntity.ok().header("id","1")
//                    .body(departmentDTO);
//        else
//        return  ResponseEntity.badRequest()
//                .header("err","123")
//                .body(departmentDTO);

        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }
    //Nếu đẩy lên dạng JSON dùng RequestBody
    //json khoogn upload đc file
    @PostMapping("/json")
    public ResponseDTO<Void> createNewJson(
            @RequestBody @Valid DepartmentDTO departmentDTO
    )
    {
        departmentServices.create(departmentDTO);
        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    //HTTP STATUS CODE LÀ 200
    @DeleteMapping("/")
    public ResponseDTO<Void> delete(@RequestParam("id") int id){
        departmentServices.delete(id);
        return ResponseDTO.<Void>builder().status(200).msg("OK").build();
    }

    @GetMapping("/")
//    @Secured({"ROLE_ADMIN","ROLE_SYSADMIN"})//ROLE_
    @RolesAllowed({"ROLE_ADMIN","ROLE_SYSADMIN"})
//    @PreAuthorize("hasRole('ROLE_ADMIN)') or hasRole('ROLE_SYSADMIN')")
    //hoặc là hàm này nếu chỉ cần xác thực
    @PreAuthorize("isAuthenticated()")
//    @ResponseStatus(code = HttpStatus.OK)
    public ResponseDTO<DepartmentDTO> get(@RequestParam("id") int id ){
        //.data chính là đọc từ db lên
        return ResponseDTO.<DepartmentDTO>builder()
                .status(200)
                .data(departmentServices.getById(id))
                .build();
    }

    @PutMapping("/")
    public ResponseDTO<DepartmentDTO> update(@ModelAttribute @Valid
                          DepartmentDTO departmentDTO){
        departmentServices.update(departmentDTO);
        return ResponseDTO.<DepartmentDTO>builder()
                .status(200)
                .data(departmentDTO)
                .build();
    }
}
