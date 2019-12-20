package org.jarvis.kk.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

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

    @GetMapping("/naverLogin")
    public void naverLoginCallback() throws UnsupportedEncodingException {
        log.info("=============================================");
        String clientId =
        String clientSecret = 
        String redirectURI = URLEncoder.encode("YOUR_CALLBACK_URL", "UTF-8");
        String apiURL;
        apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
        apiURL += "client_id=" + clientId;
        apiURL += "&client_secret=" + clientSecret;
        apiURL += "&redirect_uri=" + redirectURI;
        RestTemplate template = new RestTemplate();
        String response = template.getForObject(apiURL, String.class);
        log.info("================="+response);
        int responseCode = 200;
        if (responseCode == 200) { // 정상 호출
        } else { // 에러 발생
        }
    }
}