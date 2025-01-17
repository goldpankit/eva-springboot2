package com.eva;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@Slf4j
@SpringBootApplication
@MapperScan("com.eva.dao")
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Application.class);
        context.getEnvironment();
    }
}
