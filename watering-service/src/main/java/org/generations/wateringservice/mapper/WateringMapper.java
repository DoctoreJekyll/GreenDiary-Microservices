package org.generations.wateringservice.mapper;

import org.generations.wateringservice.dto.WateringDTO;
import org.generations.wateringservice.model.Watering;
import org.springframework.stereotype.Component;

@Component
public class WateringMapper {

    public WateringDTO fromWateringToWateringDTO(Watering watering) {
        WateringDTO wateringDTO = new WateringDTO();
        wateringDTO.setId(watering.getId());
        wateringDTO.setPlantId(watering.getPlantId());
        wateringDTO.setWateringDate(watering.getWateringDate());
        wateringDTO.setNotes(watering.getNotes());

        return wateringDTO;
    }

    public Watering fromWateringDTOtoWatering(WateringDTO wateringDTO) {
        Watering watering = new Watering();
        watering.setId(wateringDTO.getId());
        watering.setPlantId(wateringDTO.getPlantId());
        watering.setWateringDate(wateringDTO.getWateringDate());
        watering.setNotes(wateringDTO.getNotes());

        return watering;
    }
}
