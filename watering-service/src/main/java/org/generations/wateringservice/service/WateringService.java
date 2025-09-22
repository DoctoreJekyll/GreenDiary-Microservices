package org.generations.wateringservice.service;

import org.generations.wateringservice.dto.WateringDTO;
import org.generations.wateringservice.mapper.WateringMapper;
import org.generations.wateringservice.model.Watering;
import org.generations.wateringservice.repository.WateringRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WateringService {
    private final WateringRepository wateringRepository;
    private final WateringMapper wateringMapper;


    public WateringService(WateringRepository wateringRepository, WateringMapper wateringMapper) {
        this.wateringRepository = wateringRepository;
        this.wateringMapper = wateringMapper;
    }

    public Optional<WateringDTO> findById(Integer id) {
        return wateringRepository.findById(id)
                .map(wateringMapper::fromWateringToWateringDTO);
    }


    public List<WateringDTO> findAll() {
        List<Watering> wateringList = wateringRepository.findAll();
        return wateringList.stream()
                .map(wateringMapper::fromWateringToWateringDTO)
                .collect(Collectors.toList());
    }

    public WateringDTO save(WateringDTO wateringDTO) {
        Watering watering = wateringMapper.fromWateringDTOtoWatering(wateringDTO);
        watering.setOwnerUsername(wateringDTO.getOwnerUsername());
        Watering savedWatering = wateringRepository.save(watering);
        return wateringMapper.fromWateringToWateringDTO(savedWatering);
    }

    public Optional<WateringDTO> update(Integer id, WateringDTO wateringDTO) {
        return wateringRepository.findById(id)
                .map(existing -> {
                    existing.setPlantId(wateringDTO.getPlantId());
                    existing.setWateringDate(wateringDTO.getWateringDate());
                    existing.setNotes(wateringDTO.getNotes());
                    Watering updated = wateringRepository.save(existing);
                    return wateringMapper.fromWateringToWateringDTO(updated);
                });
    }

    public void deleteById(Integer id) {
        wateringRepository.deleteById(id);
    }
}
