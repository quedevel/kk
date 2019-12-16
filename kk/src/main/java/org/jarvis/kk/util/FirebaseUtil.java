package org.jarvis.kk.util;

import java.util.Arrays;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;

/**
 * FirebaseUtil
 */
public final class FirebaseUtil {

    private static final FirebaseApp firebaseApp;

    static {
        firebaseApp = FirebaseApp.getInstance("options");
    }

    public static void addAllTopics(String token) {
        try {
            FirebaseMessaging.getInstance(firebaseApp).subscribeToTopic(Arrays.asList(token), "All");    
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public static void pushAllFcm() {
        Notification notification = Notification.builder().setTitle("Knock Knock").setBody("특가 정보가 도착했습니다!").setImage("chrome-extension://gojmfdiadefnfndenopbbjcioiigfkbi/msgImg.png").build();

        Message message = Message.builder().putData("score", "850").putData("time", "2:45")
                .setWebpushConfig(WebpushConfig.builder().setFcmOptions(WebpushFcmOptions.withLink("chrome-extension://gojmfdiadefnfndenopbbjcioiigfkbi/")).build())
                .setNotification(notification)
                .setTopic("All")
                .build();
        try {
            FirebaseMessaging.getInstance(firebaseApp).send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}