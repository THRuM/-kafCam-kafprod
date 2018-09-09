package org.app.kafprod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafprodApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafprodApplication.class, args);
    }
}

