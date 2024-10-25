package com.example.springSWE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.springSWE.model")
public class SpringSWEApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSWEApplication.class, args);
    }

}
