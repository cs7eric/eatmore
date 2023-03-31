package com.cs7eric.eatmore;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@EnableCaching
@SpringBootApplication
@MapperScan("com.cs7eric.eatmore.mapper")
@ServletComponentScan("com.cs7eric.eatmore.filter")
@EnableTransactionManagement
public class EatmoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(EatmoreApplication.class, args);
        log.info("启动成功");
    }

}


