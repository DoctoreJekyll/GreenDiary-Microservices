package org.generations.wateringservice;

import org.generations.wateringservice.model.Watering;
import org.generations.wateringservice.repository.WateringRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableFeignClients
public class WateringServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WateringServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(WateringRepository repository)
    {
        return args -> {
            if (repository.count() == 0) {
                Watering watering = Watering.builder()
                        .plantId(1)
                        .wateringDate(LocalDateTime.now())
                        .notes("test")
                        .ownerUsername("jose")
                        .build();

                repository.save(watering);
            }
        };
    }

}


