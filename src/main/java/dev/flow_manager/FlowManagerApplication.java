package dev.flow_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FlowManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlowManagerApplication.class, args);
    }

}
