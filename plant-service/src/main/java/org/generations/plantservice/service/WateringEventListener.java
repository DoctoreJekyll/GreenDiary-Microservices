package org.generations.plantservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.generations.plantservice.dto.WateringEvent;
import org.generations.plantservice.model.Plant;
import org.generations.plantservice.repository.PlantRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class WateringEventListener {

    private final PlantRepository plantRepository;
    private final ObjectMapper objectMapper;

    public WateringEventListener(PlantRepository plantRepository, ObjectMapper objectMapper) {
        this.plantRepository = plantRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "watering-events", groupId = "plant-service-group")
    public void handleWateringEvent(String message) {
        try {
            WateringEvent event = objectMapper.readValue(message, WateringEvent.class);
            Plant plant = plantRepository.findById(event.getPlantId())
                .orElseThrow(() -> new RuntimeException("Planta no encontrada"));
            plant.setLastWatered(event.getWateredAt());
            plantRepository.save(plant);
        } catch (Exception e) {
            // log error
        }
    }
}
