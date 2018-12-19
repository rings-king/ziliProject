package com.jux.mtqiushui.auth.config;

import com.jux.mtqiushui.auth.security.DomainUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;

/**
 * Created by wangyunfei on 2017/6/9.
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {



    @Bean
    public UserDetailsService userDetailsService(){
        return new DomainUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }
    @Override
    public void configure(WebSecurity http) throws Exception {
        http .ignoring().antMatchers("/v1/users/registerUser")
             .and().ignoring().antMatchers("/v1/users/valiCode")
             .and().ignoring().antMatchers("/v1/users/proofCheckCode")
             .and().ignoring().antMatchers("/v1/users/isExistsUserName")
             .and().ignoring().antMatchers("/v1/users/isExistsEmail")
             .and().ignoring().antMatchers("/v1/users/isExistsPhone");

//                .and().ignoring().antMatchers("/v1/users/findUserIdByUsername")
//                .and().ignoring().antMatchers("/v1/organizations/**")
    }
    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    //不定义没有password grant_type
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



}
