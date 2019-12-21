package org.jarvis.kk.security;

import org.jarvis.kk.service.FCMService;
import org.jarvis.kk.service.OAuth2MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

/**
 * SecurityConfig
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2MemberService oauth2MemberService;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final FCMService fcmService;

    @Bean
    public AuthenticationSuccessHandler restAuthenticationLoginSuccessHandler() {
        return new RestAuthenticationLoginSuccessHandler(fcmService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
        // .and().headers().frameOptions().disable()
        .and().authorizeRequests().antMatchers("/kk/token").permitAll().anyRequest().authenticated()
        .and().logout().logoutSuccessUrl("/")
        .and().oauth2Login().successHandler(restAuthenticationLoginSuccessHandler()).userInfoEndpoint().userService(oauth2MemberService);
    }
    
}