package net.okasian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = {
        WebMvcAutoConfiguration.class
})
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
