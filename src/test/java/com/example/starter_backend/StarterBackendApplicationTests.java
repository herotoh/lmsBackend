package com.example.starter_backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootTest
@ActiveProfiles("test")
@SpringBootApplication
@EnableScheduling
class StarterBackendApplicationTests {

    @Test
    void contextLoads() {
    }
}