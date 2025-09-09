package org.generations.wateringservice.repository;

import org.generations.wateringservice.model.Watering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WateringRepository extends JpaRepository<Watering,Integer> {
}
