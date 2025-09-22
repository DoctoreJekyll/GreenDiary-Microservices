package org.generations.authservice;

import org.generations.authservice.model.UserApp;
import org.generations.authservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner seed(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByUsername("jose").isEmpty()) {
                UserApp admin = UserApp.builder()
                        .username("jose")
                        .password(encoder.encode("admin"))
                        .roles(new java.util.HashSet<>(java.util.List.of("ROLE_ADMIN")))
                        .build();
                repo.save(admin);
            }
        };
    }

}
