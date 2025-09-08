package org.generations.plantservice.controller;

import org.generations.plantservice.dto.PlantDTO;
import org.generations.plantservice.model.Plant;
import org.generations.plantservice.service.PlantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/plants")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping
    public ResponseEntity<List<PlantDTO>> getAllPlants() {
        return ResponseEntity.ok(plantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantDTO> getPlantById(@PathVariable int id) {
        Optional<PlantDTO> plantOptional = plantService.findById(id);
        return ResponseEntity.ok(plantOptional.orElseThrow());
    }

    @PostMapping
    public ResponseEntity<PlantDTO> createPlant(@RequestBody PlantDTO plantDTO) {
        return ResponseEntity.ok(plantService.createPlantDTO(plantDTO));
    }
}
