package org.generations.plantservice.repository;

import org.generations.plantservice.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Integer> {

    List<Plant> findByOwnerUsername(String ownerUsername);

}
