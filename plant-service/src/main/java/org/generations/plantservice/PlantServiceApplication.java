package org.generations.plantservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PlantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlantServiceApplication.class, args);
    }

}
