package org.generations.plantservice.mapper;

import org.generations.plantservice.dto.PlantDTO;
import org.generations.plantservice.model.Plant;
import org.springframework.stereotype.Component;

@Component
public class PlantMapper {

    public PlantDTO toPlantDTO(Plant plant) {
        PlantDTO plantDTO = new PlantDTO();
        plantDTO.setId(plant.getId());
        plantDTO.setName(plant.getName());
        plantDTO.setSpecies(plant.getSpecies());
        plantDTO.setLocation(plant.getLocation());
        plantDTO.setNotes(plant.getNotes());
        plantDTO.setLastWatered(plant.getLastWatered());
        return plantDTO;
    }

    public Plant toPlant(PlantDTO plantDTO) {
        Plant plant = new Plant();
        plant.setId(plantDTO.getId());
        plant.setName(plantDTO.getName());
        plant.setSpecies(plantDTO.getSpecies());
        plant.setLocation(plantDTO.getLocation());
        plant.setNotes(plantDTO.getNotes());
        plant.setLastWatered(plantDTO.getLastWatered());
        return plant;
    }


}
