package com.example.testspring.controller;

import com.example.testspring.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cache")
public class CacheController {
    @Autowired
    CacheManager cacheManager;

    @GetMapping("/")
    public List<String> getCache(){
        return cacheManager.getCacheNames().stream().collect(Collectors.toList());
    }
    //Thay vi minh ? tren duong dan thi co the viet nhu the nay
    @DeleteMapping("/")
    public ResponseDTO<Void> deleteCache(
            @RequestParam("name") String name
            ){
        Cache cache = cacheManager.getCache(name);
        if (cache!=null){
            cache.clear();//hoac invalidate
        }
        return ResponseDTO.<Void>builder().status(200).build();
    }
}
