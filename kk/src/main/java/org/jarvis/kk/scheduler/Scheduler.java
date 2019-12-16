package org.jarvis.kk.scheduler;

import java.nio.charset.Charset;

import org.jarvis.kk.util.FirebaseUtil;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Scheduler
 */
@Component
public class Scheduler {

    @Scheduled(cron = "0 0 10,15,20 * * *")
    private void rambdaTest() {
        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        template.getForObject("https://09wnfs0mt0.execute-api.ap-northeast-2.amazonaws.com/default/CommunityCrawling", String.class);
        FirebaseUtil.pushAllFcm();
    }
}