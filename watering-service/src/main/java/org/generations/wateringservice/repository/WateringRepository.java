package org.generations.wateringservice.repository;

import org.generations.wateringservice.model.Watering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WateringRepository extends JpaRepository<Watering,Integer> {
    List<Watering> findByPlantId(Integer plantId);
    List<Watering> findByOwnerUsername(String ownerUsername);
    Optional<Watering> findByIdAndOwnerUsername(Integer id, String ownerUsername);
}
