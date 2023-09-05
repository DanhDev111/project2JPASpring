package com.example.testspring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true ,
        prePostEnabled = true ,jsr250Enabled = true)
public class SecurityConfig {
    // 1 la lam bao mat theo duong dan
    // giống như trong servlet
    // Authentication: xác thực
    @Autowired
    JwTtokenFilter jwTtokenFilter;
    @Autowired
    UserDetailsService userDetailsService;

    //Chú ý khi @Autowired trong hàm sẽ gắn ở biến tham số truyền vào auth(auth)
    @Autowired
    public void config(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
    //md5,bean
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain config(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeRequests()
                .antMatchers("/admin/**","/cache/**")
                .hasAnyRole("ADMIN")
//                .hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/member/**")
                .authenticated()
                .anyRequest().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and().httpBasic()
                .and().csrf().disable();

        // chèn filter apply filter
        httpSecurity.addFilterBefore(jwTtokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
