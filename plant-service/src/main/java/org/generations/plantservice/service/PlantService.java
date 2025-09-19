package org.generations.plantservice.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.generations.commonlib.exception.ResourceNotFoundException;
import org.generations.plantservice.dto.PlantDTO;
import org.generations.plantservice.dto.PlantWithWateringDTO;
import org.generations.plantservice.dto.WateringDTO;
import org.generations.plantservice.mapper.PlantMapper;
import org.generations.plantservice.model.Plant;
import org.generations.plantservice.repository.PlantRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlantService {
    private final PlantRepository plantRepository;
    private final PlantMapper plantMapper;
    private final WateringClient wateringClient;

    public PlantWithWateringDTO getPlantWithLastWatering(int plantId){

        PlantDTO plantDTO = plantMapper
                .toPlantDTO(plantRepository.findById(plantId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Plant not found with id: " +   plantId)));

        WateringDTO wateringDTO = wateringClient.getWatering(plantId);

        return new PlantWithWateringDTO(plantDTO,wateringDTO);
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

    public WateringDTO addWateringToPlant(int plantId, WateringDTO wateringDTO, String username) {
        // 1. Validar que la planta pertenece al usuario:
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new ResourceNotFoundException("Plant not found " + plantId));

        if (!plant.getOwnerUsername().equals(username)) {
            throw new AccessDeniedException("This plant does not belong to you");
        }

        // 2. Forzar plantId en el DTO
        wateringDTO.setPlantId(plantId);

        if (wateringDTO.getWaterTime() == null) {
            wateringDTO.setWaterTime(LocalDateTime.now());
        }

        // 3. Llamar al watering-service
        return wateringClient.createWatering(plantId, wateringDTO);
    }

}
