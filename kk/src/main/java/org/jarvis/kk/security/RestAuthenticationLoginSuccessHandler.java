package org.jarvis.kk.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jarvis.kk.service.FCMService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * RestAuthenticationLoginSuccessHandler
 */
@Slf4j
@RequiredArgsConstructor
public class RestAuthenticationLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final FCMService fcmService;
   
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info(request.getHeader("X-FORWARDED-FOR"));
        log.info(request.getRemoteAddr());
        response.sendRedirect("/kk/loginSuccess");
        fcmService.pushOneFcm();
        // log.info(request.getContextPath());
        // log.info(request.getRequestURL() + "");
        // log.info(request.getAttribute("token")+"");
        // request.getRequestDispatcher(request.getRequestURI()).forward(request, response);
    }
}