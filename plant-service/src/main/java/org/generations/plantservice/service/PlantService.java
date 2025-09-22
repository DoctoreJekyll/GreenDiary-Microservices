package org.generations.plantservice.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.generations.commonlib.exception.ResourceNotFoundException;
import org.generations.plantservice.dto.PlantDTO;
import org.generations.plantservice.dto.PlantWithWateringDTO;
import org.generations.plantservice.dto.WateringDTO;
import org.generations.plantservice.mapper.PlantMapper;
import org.generations.plantservice.model.Plant;
import org.generations.plantservice.repository.PlantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlantService {
    private final PlantRepository plantRepository;
    private final PlantMapper plantMapper;
    private final WateringClient wateringClient;

    public PlantWithWateringDTO getPlantWithLastWatering(int plantId) {
        System.out.println(">>> Buscando planta " + plantId);
        PlantDTO plantDTO = plantRepository.findById(plantId)
                .map(plantMapper::toPlantDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found " + plantId));

        // Llama al client y verifica la respuesta
        ResponseEntity<WateringDTO> response = wateringClient.getWatering(plantId);
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            // Maneja el caso de error (por ejemplo, planta no encontrada)
            throw new ResourceNotFoundException("Watering data not found for plant " + plantId);
        }

        WateringDTO wateringDTO = response.getBody();
        log.info("Watering recibido desde watering-service: {}", wateringDTO);

        PlantWithWateringDTO dto = new PlantWithWateringDTO();
        dto.setPlantDTO(plantDTO);
        dto.setWateringDTO(wateringDTO);
        System.out.println(">>> Construido PlantWithWateringDTO: " + dto);
        return dto;
    }

    public Optional<PlantDTO> findById(Integer id) {
        return plantRepository.findById(id)
                .map(plantMapper::toPlantDTO);
    }

    public List<PlantDTO> findAll() {
        return plantRepository.findAll()
                .stream()
                .map(plantMapper::toPlantDTO)
                .collect(Collectors.toList());
    }

    public List<PlantDTO> findAllByOwner(String ownerUsername) {
        return plantRepository.findByOwnerUsername(ownerUsername)
                .stream()
                .map(plantMapper::toPlantDTO)
                .toList();
    }

    public PlantDTO createPlantDTO(PlantDTO plantDTO, String username) {
        Plant plant = plantMapper.toPlant(plantDTO);
        plant.setOwnerUsername(username);
        Plant saved = plantRepository.save(plant);
        return plantMapper.toPlantDTO(saved);
    }

    public Optional<PlantDTO> updatePlantDTO(Integer id, PlantDTO plantDTO) {
        // Busca la planta por ID.
        return plantRepository.findById(id)
                .map(existingPlant -> {
                    // Si la planta existe, actualiza sus campos con los datos del DTO.
                    existingPlant.setName(plantDTO.getName());
                    existingPlant.setLocation(plantDTO.getLocation());
                    existingPlant.setNotes(plantDTO.getNotes());
                    existingPlant.setSpecies(plantDTO.getSpecies());
                    existingPlant.setLastWatered(plantDTO.getLastWatered());

                    // Guarda la entidad actualizada en la base de datos.
                    Plant updatedPlant = plantRepository.save(existingPlant);

                    // Mapea la entidad actualizada a un DTO y lo devuelve en un Optional.
                    return plantMapper.toPlantDTO(updatedPlant);
                });
    }

    public void deletePlant(Integer id) {
        plantRepository.deleteById(id);
    }

    // En tu archivo PlantService.java
    public WateringDTO addWateringToPlant(int plantId, WateringDTO wateringDTO, String username) {
        // 1. Validar que la planta pertenece al usuario:
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found " + plantId));

        if (!plant.getOwnerUsername().equals(username)) {
            throw new AccessDeniedException("This plant does not belong to you");
        }

        // 2. Forzar plantId y ownerUsername en el DTO
        wateringDTO.setPlantId(plantId);
        wateringDTO.setOwnerUsername(username); // <- ¡Añade esta línea!

        if (wateringDTO.getWateringDate() == null) {
            wateringDTO.setWateringDate(LocalDateTime.now());
        }

        ResponseEntity<WateringDTO> response = wateringClient.createWatering(plantId, wateringDTO);
        return response.getBody();
    }

}
