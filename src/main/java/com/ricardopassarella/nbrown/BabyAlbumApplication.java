package com.ricardopassarella.nbrown;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Ricardo on 2019-03-12.
 */
@SpringBootApplication
public class BabyAlbumApplication {

    public static void main(String[] args) {
//        if (!StringUtils.hasText(System.getProperty("spring.profiles.active")) &&
//                !StringUtils.hasText(System.getenv("SPRING_PROFILES_ACTIVE"))) {
//            System.setProperty("spring.profiles.active", "local");
//        }
        SpringApplication.run(BabyAlbumApplication.class, args);
    }
}
