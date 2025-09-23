package org.generations.plantservice;

import org.generations.plantservice.model.Plant;
import org.generations.plantservice.repository.PlantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableFeignClients
public class PlantServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlantServiceApplication.class, args);
    }

}
