package org.generations.plantservice;

import org.generations.plantservice.model.Plant;
import org.generations.plantservice.repository.PlantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PlantIntegrationTest {

    @Autowired
    PlantRepository repo;

}
