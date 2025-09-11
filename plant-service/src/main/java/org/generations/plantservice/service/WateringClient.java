package org.generations.plantservice.service;

import org.generations.plantservice.dto.WateringDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "watering-service")
public interface WateringClient {

    @GetMapping("/api/watering/plants/{plantId}")
    WateringDTO getWatering(@PathVariable("plantId") int id);

}
