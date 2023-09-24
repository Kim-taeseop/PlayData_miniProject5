package com.kts.tku2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.kts.tku2.data.entity") // 엔터티 클래스 패키지 경로 지정
public class Tku2Application {

    public static void main(String[] args) {
        SpringApplication.run(Tku2Application.class, args);
    }

}
