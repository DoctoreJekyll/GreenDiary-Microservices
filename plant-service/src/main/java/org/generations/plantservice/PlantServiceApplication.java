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

    @Bean
    CommandLineRunner seed(PlantRepository repo) {
        return args -> {
            if (repo.findById(1).isEmpty()) {
                Plant plant = Plant.builder()
                        .name("Rosa")
                        .species("Super rosa")
                        .location("Salon")
                        .notes("Notas")
                        .lastWatered(LocalDateTime.now().toString())
                        .ownerUsername("jose").build();

                repo.save(plant);
            }
        };
    }


}
