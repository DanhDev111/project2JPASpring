package com.example.testspring.repository;
//lop repo con co the goi la dao


import com.example.testspring.dto.UserDTO;
import com.example.testspring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo
        extends JpaRepository<User,Integer>{
        //Tim theo username
    // select user where username = ?
    User findByUsername(String username);
    //where name = :s
    Page<User> findByName(String s, Pageable pageable);
    // Khác sql ở chỗ mình ko select trên bảng mà select theo class User
    // có User nó là entity không phải table
    // mình select trên class entity chứ không trên table
    @Query("SELECT u FROM User u WHERE u.name LIKE :x")
    // Để map biến x với tham số truyên vào chỉ cần
    Page<User> searchByName(@Param("x") String s,Pageable pageable);

    //Search theo name hoặc username
    @Query("SELECT u FROM User u WHERE u.name LIKE :x " +
            "OR u.username LIKE :x")
    List<User> searchByNameOrUsername(@Param("x") String s);

    @Modifying
    @Query("DELETE FROM User u WHERE u.username = :x")
    int deleteUser(@Param("x") String s);

//    void deleteByRoleId(long roleId);

    // hoawcj tu gen lenh xoas
    void deleteByUsername(String username);

    @Query("SELECT u FROM User u WHERE MONTH(u.birthDate) =:month AND DAY(u.birthDate) =:date")
    List<User> searchUserHasBirthDay(@Param("date") int date,
                                     @Param("month") int month);
}
