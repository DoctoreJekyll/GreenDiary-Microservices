package org.generations.plantservice.service;

import org.generations.plantservice.dto.WateringDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "watering-service")
public interface WateringClient {

    //cuando alguien use esta interfaz, en realidad ve a hablar con el watering-service en Eureka
    @GetMapping("/api/watering/plants/{plantId}")
    WateringDTO getWatering(@PathVariable("plantId") int id);

    @PostMapping("/api/watering/plants/{plantId}")
    WateringDTO createWatering(@PathVariable("plantId") int plantId,
                               @RequestBody WateringDTO wateringDTO);

}
