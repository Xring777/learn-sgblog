package com.liuzihan.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.liuzihan.*")
@MapperScan("com.liuzihan.framework.mapper")
public class SanGengAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(SanGengAdminApplication.class,args);
    }
}
