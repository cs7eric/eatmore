package com.cs7eric.eatmore;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@MapperScan("com.cs7eric.eatmore.mapper")
public class EatmoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(EatmoreApplication.class, args);
        log.info("启动成功");
    }

}
