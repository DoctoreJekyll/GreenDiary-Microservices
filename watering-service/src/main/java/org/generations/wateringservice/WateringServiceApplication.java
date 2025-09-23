package org.generations.wateringservice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class WateringServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WateringServiceApplication.class, args);
    }

}


