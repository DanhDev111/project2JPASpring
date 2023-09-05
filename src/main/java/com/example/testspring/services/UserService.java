package com.example.testspring.services;

import com.example.testspring.dto.PageDTO;
import com.example.testspring.dto.SearchDTO;
import com.example.testspring.entity.User;
import com.example.testspring.dto.UserDTO;
import com.example.testspring.repository.DepartmentRepo;
import com.example.testspring.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@Component
// Tuy tao the nao cung dc dugn de tao bean
@Service //tao Bean: new UserService, qly SpringContainer
public class UserService implements UserDetailsService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    DepartmentRepo departmentRepo;
    // Thuong cai ham mình tạo sẽ cho vào 1 cái transaction
    // Để đảm bảo dữ liệu miình thêm sửa xóa tránh dữ liệu bị sai
    // Nhỡ có 1 thằng bị sai nó sẽ rollback để ko
    // Thang thứ 1 , thứ 2 insert đc rồi còn ông thứ 3
    // ko đc sẽ rollback lại để tránh database bị sai

    @Transactional
    public void create(UserDTO userDTO){
        //convert userdto -> user
        // Convert từ UserDTO sang User trước khi lưu vào cơ sở dữ liệu

        User user = new ModelMapper().map(userDTO, User.class);
        //convert password to bcrypt
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepo.save(user);
    }

    @Transactional
    public void delete(int id){
        userRepo.deleteById(id);
    }

    //Luu y update voi them moi dung chung ham voi nhau save()
    @Transactional
    public void update(UserDTO userDTO){
        //check xem co ton tai hay khong
        // neu co thi moi update
        User currentUser = userRepo.findById(userDTO.getId()).orElse(null);
        if (currentUser != null){
            currentUser = new ModelMapper().map(userDTO,User.class);
//            currentUser.setName(userDTO.getName());
//            currentUser.setAge(userDTO.getAge());
//            currentUser.setUsername(userDTO.getUsername());
//            currentUser.setDepartment(departmentRepo.findById(userDTO.getDepartment().getId()).orElse(null));
//            currentUser.setHomeAddress(userDTO.getHomeAddress());
//            currentUser.setBirthDate(userDTO.getBirthDate());


            if (userDTO.getAvatarURL()!=null)
                currentUser.setAvatarURL(userDTO.getAvatarURL());
        }
            userRepo.save(currentUser);
    }
    @Transactional
    public void updatePassword(UserDTO user){
        //check xem co ton tai hay khong
        // neu co thi moi update
        User currentUser = userRepo.findById(user.getId()).orElse(null);
        if (currentUser !=null){
            currentUser.setPassword(user.getPassword());
            userRepo.save(currentUser);
        }
    }

    public UserDTO getById(int id){
        //Neu tim thay thi tra ve user con khong thi tra ve null
        //Kieu optinal java 8
        User user = userRepo.findById(id).orElse(null);
        if (user !=null){

            return convert(user);
        }
        return null;
    }
    public UserDTO convert(User user){
//        UserDTO userDTO = new UserDTO();
//        userDTO.setId(user.getId());
//        userDTO.setName(user.getName());
//        userDTO.setAge(user.getAge());
//        userDTO.setUsername(user.getUsername());
//        userDTO.setPassword(user.getPassword());
//        userDTO.setAvatarURL(user.getAvatarURL());
//        userDTO.setHomeAddress(user.getHomeAddress());
        //convert sang thi co the lam nguoc lai
        return new ModelMapper().map(user, UserDTO.class);
    }
    public List<UserDTO> getAll(){
        List<User> userList = userRepo.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
//        for (User user : userList){
//            userDTOList.add(convert(user));
//        }
//        return userDTOList;
        // trogn java 8 co the viet nhu the nay
        return userList.stream().map(u ->convert(u)).collect(Collectors.toList());
    }

    public PageDTO<List<UserDTO>> searchName(SearchDTO searchDTO){
        // ham nay ra tu Interface Repo

        Sort sortBy = Sort.by("name").ascending().and(Sort.by("age").descending());

//        if (sortBy !=null && !sortBy.isEmpty()){
//
//        }
        if (StringUtils.hasText(searchDTO.getSortedField())){
            sortBy=  Sort.by(searchDTO.getSortedField()).descending();
        }
        if (searchDTO.getCurrentPage()==null){
            searchDTO.setCurrentPage(0);
        }
        if (searchDTO.getSize()==null){
            searchDTO.setSize(5);
        }
        if (searchDTO.getKeyword()==null){
            searchDTO.setKeyword("");
        }

        PageRequest pageRequest =
                PageRequest.of(searchDTO.getCurrentPage(),searchDTO.getSize(),sortBy);
        Page<User> page = userRepo.searchByName("%"+searchDTO.getKeyword()+"%", pageRequest);

        PageDTO<List<UserDTO>> pageDTO = new PageDTO<>();
        pageDTO.setTotalPages(page.getTotalPages());
        pageDTO.setTotalElements(page.getTotalElements());

        //Mình sẽ phải convert List<User> sang UserDTO
        List<UserDTO> userDTOS =
                page.get().map(u ->convert(u)).collect(Collectors.toList());
        pageDTO.setData(userDTOS);

//        List<User> users =  page.getContent();
        // hoac la viet the nay
//        return userRepo.searchByName("%"+name+"%");
        return pageDTO;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userRepo.findByUsername(username);
        if (userEntity==null){
            throw new UsernameNotFoundException("Not Found!");
        }
        //convert user của mình -> userdetails
        List<SimpleGrantedAuthority> authorities =
                new ArrayList<>();
        userEntity.getRoles();
        for (String role: userEntity.getRoles()){
            //truyền vai trò về role trong security
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return new org.springframework.security.core.userdetails.User(
                username, userEntity.getPassword(), authorities);
    }
}
