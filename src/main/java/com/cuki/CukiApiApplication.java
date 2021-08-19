package com.cuki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CukiApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CukiApiApplication.class, args);
    }

}
