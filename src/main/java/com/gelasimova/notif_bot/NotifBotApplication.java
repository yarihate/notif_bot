package com.gelasimova.notif_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class NotifBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(NotifBotApplication.class, args);
    }
}
