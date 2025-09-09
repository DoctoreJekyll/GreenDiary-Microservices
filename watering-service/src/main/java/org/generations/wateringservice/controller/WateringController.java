package org.generations.wateringservice.controller;

import org.generations.wateringservice.dto.WateringDTO;
import org.generations.wateringservice.service.WateringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/watering")
public class WateringController {
    private final WateringService wateringService;

    public WateringController(WateringService wateringService) {
        this.wateringService = wateringService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<WateringDTO> getWatering(@PathVariable("id") Integer id) {
        return wateringService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<WateringDTO>> getWateringList() {
        return ResponseEntity.ok(wateringService.findAll());
    }

    @PostMapping
    public ResponseEntity<WateringDTO> createWatering(@RequestBody WateringDTO wateringDTO) {
        WateringDTO wateringSaved = wateringService.save(wateringDTO);
        return ResponseEntity.created(URI.create("/api/watering/" + wateringSaved.getId()))
                .body(wateringSaved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WateringDTO> updateWatering(@PathVariable int id, @RequestBody WateringDTO wateringDTO) {
        return wateringService.update(id, wateringDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WateringDTO> deleteWatering(@PathVariable("id") int id) {
        wateringService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
