package org.jarvis.kk.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.jarvis.kk.domain.CommunityCrawling;
import org.jarvis.kk.repositories.CommunityCrawlingRepository;
import org.jarvis.kk.service.FCMService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * RestController
 */
@CrossOrigin
@RestController
@RequestMapping("/kk")
@RequiredArgsConstructor
@Slf4j
public class RestAPIController {

    private final CommunityCrawlingRepository communityCrawlingRepository;

    private final FCMService fcmService;

    @PostMapping("/token")
    public ResponseEntity<String> registerToken(@RequestBody String token) {
        log.info(token);
        fcmService.addAllTopics(token);
        return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CommunityCrawling>> getList() {
        return new ResponseEntity<>(
                communityCrawlingRepository.findAll(PageRequest.of(0, 50, Sort.Direction.DESC, "no")).toList(),
                HttpStatus.OK);
    }

    @GetMapping("/msg")
    public void pushAllFcm() {
        fcmService.pushAllFcm();
    }

    @GetMapping("/loginSuccess")
    public void loginSuccess(HttpServletResponse response) throws IOException{
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out =  response.getWriter();
        out.println("<script>window.close()</script>");
        out.flush();
    }
}