package org.generations.authservice.repository;


import org.generations.authservice.model.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserApp, Long> {
  Optional<UserApp> findByUsername(String username);
}
