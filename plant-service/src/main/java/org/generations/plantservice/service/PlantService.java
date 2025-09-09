package org.generations.plantservice.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.generations.plantservice.dto.PlantDTO;
import org.generations.plantservice.mapper.PlantMapper;
import org.generations.plantservice.model.Plant;
import org.generations.plantservice.repository.PlantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlantService {
    private final PlantRepository plantRepository;
    private final PlantMapper plantMapper;


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

    public PlantDTO createPlantDTO(PlantDTO plantDTO) {
        Plant plant = plantMapper.toPlant(plantDTO);
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
}
