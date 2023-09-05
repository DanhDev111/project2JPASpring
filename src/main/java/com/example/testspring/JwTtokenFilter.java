package com.example.testspring;

import com.example.testspring.services.JwTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwTtokenFilter extends OncePerRequestFilter {
    @Autowired
    JwTokenService jwTokenService;

    @Autowired
    UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // TO DO ví dụ sau này đi làm nguời ta yêu cầu key là ABCXYZ
        // thì trong postman mình sẽ đổi lại
        String bearToken = request.getHeader("Authorization");
        log.info(bearToken);
        //nhớ bearer có dấu cách
        if (bearToken != null && bearToken.startsWith("Bearer ")) {
            String token = bearToken.substring(7);
            String username = jwTokenService.getUserName(token);
            // lưu vào authority đỡ mất công gọi vào database
            if (username != null) {
                // bây h đi fake 1 cái authentication
                //token valid
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,"",
                                userDetails.getAuthorities());
                //giả lập security
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request,response);
    }
}

