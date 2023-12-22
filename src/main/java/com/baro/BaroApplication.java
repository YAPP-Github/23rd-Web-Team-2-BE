package com.baro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class BaroApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaroApplication.class, args);
    }

}
