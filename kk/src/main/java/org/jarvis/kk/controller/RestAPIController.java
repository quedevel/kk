package org.jarvis.kk.controller;

import java.nio.charset.Charset;
import java.util.List;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;

import org.jarvis.kk.domain.CommunityCrawling;
import org.jarvis.kk.repositories.CommunityCrawlingRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * RestController
 */
@CrossOrigin
@RestController
@RequestMapping("/kk")
@AllArgsConstructor
@Slf4j
public class RestAPIController {

    private CommunityCrawlingRepository communityCrawlingRepository;

    // @GetMapping(value = "/check")
    // public ResponseEntity<String> check(@RequestParam final String url) {
    // RestTemplate template = new RestTemplate();
    // template.getMessageConverters().add(0, new
    // StringHttpMessageConverter(Charset.forName("UTF-8")));
    // String response = template.getForObject("http://127.0.0.1:8000/check?url=" +
    // url, String.class);
    // return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    @GetMapping(value = "/test", produces = "text/plain;charset=UTF-8")
    public void fcmTest() throws Exception {    
        Notification notification = null;

        notification = Notification.builder().setTitle("COSME-PICK").setBody("[아워홈][아워홈] 지리산수 2L x 6병")
                .setImage("C:/cosmepick/icon.png").build();

        Message message = Message.builder().putData("score", "850").putData("time", "2:45")
                .setWebpushConfig(WebpushConfig.builder().setFcmOptions(WebpushFcmOptions.withLink(
                        "http://www.11st.co.kr/product/SellerProductDetail.tmall?method=getSellerProductDetail&prdNo=2625079984&trTypeCd=22&trCtgrNo=895019"))
                        .build())
                .setNotification(notification)
                .setToken(
                        "f5IRW9HOrFq1SVX4ZKWYji:APA91bEI51gVf0z9z-M2nYP2IawzIkzei-rqRPTaVAe6cms_C01J07HOkL3pt6ypTn0Pp6ismL_uyGolfr23Jec_EUoP523XiYL0F6b0f1YXFvSxhUuCI272arVnbNOC1pMs-fDwEFt3")
                .build();
        try {
            FirebaseMessaging.getInstance(FirebaseApp.getInstance("options")).send(message);
        } catch (final FirebaseMessagingException e) {
            e.printStackTrace();
        }

    }

    @Scheduled(cron = "0 0 10,15,20 * * *")
    @GetMapping("/lambda")
    public void rambdaTest() {
       log.info("tttttttttttttttttttttttttttttttttttt");
        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        String response = template.getForObject(
                "https://09wnfs0mt0.execute-api.ap-northeast-2.amazonaws.com/default/CommunityCrawling", String.class);
        log.info(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CommunityCrawling>> getList(){
        return new ResponseEntity<>(communityCrawlingRepository.findAll(PageRequest.of(0, 10, Sort.Direction.DESC, "no")).toList(), HttpStatus.OK);
    }
}