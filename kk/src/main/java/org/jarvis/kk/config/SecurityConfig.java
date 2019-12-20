package org.jarvis.kk.config;

import org.jarvis.kk.dto.Role;
import org.jarvis.kk.service.OAuth2MemberService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.RequiredArgsConstructor;

/**
 * SecurityConfig
 */
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2MemberService oauth2MemberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
        .headers().frameOptions().disable()
        // .and().authorizeRequests().antMatchers("").permitAll().antMatchers("").hasRole(Role.MEMBER.name()).anyRequest().authenticated()
        .and().authorizeRequests().antMatchers("/").permitAll()
        .and().logout().logoutSuccessUrl("/")
        .and().oauth2Login().userInfoEndpoint().userService(oauth2MemberService);
    }
    
}