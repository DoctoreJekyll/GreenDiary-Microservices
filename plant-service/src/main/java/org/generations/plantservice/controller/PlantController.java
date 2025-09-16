package org.generations.plantservice.controller;

import org.generations.commonlib.exception.ResourceNotFoundException;
import org.generations.plantservice.dto.PlantDTO;
import org.generations.plantservice.dto.PlantWithWateringDTO;
import org.generations.plantservice.model.Plant;
import org.generations.plantservice.service.PlantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/plants")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}/with-watering")
    public PlantWithWateringDTO getPlantWithWatering(@PathVariable("id") int id) {
        return plantService.getPlantWithLastWatering(id);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<List<PlantDTO>> getAllPlants() {
        return ResponseEntity.ok(plantService.findAll());
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PlantDTO> getPlantById(@PathVariable("id") int id) {
        return plantService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found " + id));
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    public ResponseEntity<PlantDTO> createPlant(@RequestBody PlantDTO plantDTO) {
        PlantDTO created = plantService.createPlantDTO(plantDTO);
        return ResponseEntity
                .created(URI.create("/api/plants/" + created.getId()))
                .body(created);
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PlantDTO> updatePlant(@PathVariable("id") int id, @RequestBody PlantDTO plantDTO) {
        return plantService.updatePlantDTO(id, plantDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable("id") int id) {
        plantService.deletePlant(id);
        return ResponseEntity.noContent().build();
    }
}
