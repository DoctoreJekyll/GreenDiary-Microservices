package org.generations.plantservice.service;

import org.generations.plantservice.dto.WateringDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "watering-service", url = "http://localhost:8082") // <- CAMBIO AQUÍ
public interface WateringClient {
    @GetMapping("/api/watering/plants/{plantId}")
    ResponseEntity<WateringDTO> getWatering(@PathVariable("plantId") int id);

    @PostMapping("/api/watering/plants/{plantId}")
    ResponseEntity<WateringDTO> createWatering(@PathVariable("plantId") int plantId,
                                               @RequestBody WateringDTO wateringDTO);
}