package io.tonme.minebot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan("io.tonme.minebot.mapper")
@SpringBootApplication(scanBasePackages = {"io.tonme.minebot"})
public class OctocrazeBotApplication {

    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(OctocrazeBotApplication.class, args);
    }

}
