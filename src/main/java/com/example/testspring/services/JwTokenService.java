package com.example.testspring.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwTokenService {
    //Thayv vif minh fix hard trong code thi minh
    @Value("${jwt.secret}")
    private String secretKey;
    private long validity = 5; //5 phut
    public String createToken(String username){

        Claims claims = Jwts.claims().setSubject(username);
//        claims.put(username,claims);
        Date now  = new Date();
        Date expire = new Date(now.getTime() + validity * 60 * 1000);

        return Jwts.builder().setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
    //Viết hàm check xem token còn hay không
    public boolean isValidToken(String token){
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            //do nothing
        }
        return false;
    }
    public String getUserName(String token){
        try{
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                .getBody()
                .getSubject();
        }catch (Exception e){
            // do nothing
            e.printStackTrace();
        }
        return null;
    }
}
