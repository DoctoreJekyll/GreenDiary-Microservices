package org.generations.plantservice.controller;

import org.generations.commonlib.exception.ResourceNotFoundException;
import org.generations.plantservice.dto.PlantDTO;
import org.generations.plantservice.dto.PlantWithWateringDTO;
import org.generations.plantservice.dto.WateringDTO;
import org.generations.plantservice.model.Plant;
import org.generations.plantservice.service.PlantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    public ResponseEntity<PlantWithWateringDTO> getPlantWithWatering(@PathVariable("id") int id) {
        System.out.println(">>> Entrando en getPlantWithWatering con id=" + id);
        PlantWithWateringDTO result = plantService.getPlantWithLastWatering(id);
        System.out.println("Sale del result???");
        System.out.println(">>> Saliendo de getPlantWithWatering con resultado=" + result);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/{id}/watering")
    public ResponseEntity<WateringDTO> addWateringToPlant(@PathVariable("id") int id,
                                                          @RequestBody WateringDTO wateringDTO,
                                                          @AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        WateringDTO created = plantService.addWateringToPlant(id, wateringDTO, username);
        return ResponseEntity.created(URI.create("/api/plants/" + id + "/watering")).body(created);
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<PlantDTO>> getAllPlants() {
        return ResponseEntity.ok(plantService.findAll());
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<PlantDTO> getPlantById(@PathVariable("id") int id, @AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        boolean isAdmin = jwt.getClaimAsStringList("roles").contains("ADMIN");

        PlantDTO plantDTO = plantService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found " + id));


        if (!isAdmin && !plantDTO.getOwnerUsername().equals(username)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(plantDTO);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping
    public ResponseEntity<List<PlantDTO>> getAllPlantsByUser(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        return ResponseEntity.ok(plantService.findAllByOwner(username));
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping
    public ResponseEntity<PlantDTO> createPlant(@RequestBody PlantDTO plantDTO, @AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        PlantDTO created = plantService.createPlantDTO(plantDTO, username);
        return ResponseEntity
                .created(URI.create("/api/plants/" + created.getId()))
                .body(created);
    }


    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PlantDTO> updatePlant(@PathVariable("id") int id, @RequestBody PlantDTO plantDTO, @AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        boolean isAdmin = jwt.getClaimAsStringList("roles").contains("ADMIN");

        plantDTO = plantService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found " + id));

        if (!isAdmin && !plantDTO.getOwnerUsername().equals(username)) {
            return ResponseEntity.status(403).build();
        }

        return plantService.updatePlantDTO(id, plantDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable("id") int id, @AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        boolean isAdmin = jwt.getClaimAsStringList("roles").contains("ADMIN");

        PlantDTO plantDTO = plantService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found " + id));

        if (!isAdmin && !plantDTO.getOwnerUsername().equals(username)) {
            return ResponseEntity.status(403).build();
        }

        plantService.deletePlant(id);
        return ResponseEntity.noContent().build();
    }
}
