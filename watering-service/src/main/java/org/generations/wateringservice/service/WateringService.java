package org.generations.wateringservice.service;

import org.generations.wateringservice.WateringEvent.WateringEvent;
import org.generations.wateringservice.WateringEvent.WateringEventProducer;
import org.generations.wateringservice.dto.WateringDTO;
import org.generations.wateringservice.mapper.WateringMapper;
import org.generations.wateringservice.model.Watering;
import org.generations.wateringservice.repository.WateringRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WateringService {
    private final WateringRepository wateringRepository;
    private final WateringMapper wateringMapper;
    private final WateringEventProducer  wateringEventProducer;


    public WateringService(WateringRepository wateringRepository, WateringMapper wateringMapper, WateringEventProducer wateringEventProducer) {
        this.wateringRepository = wateringRepository;
        this.wateringMapper = wateringMapper;
        this.wateringEventProducer = wateringEventProducer;
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

        wateringEventProducer.sendWateringEvent(wateringEvent(wateringMapper.fromWateringToWateringDTO(savedWatering)));

        return wateringMapper.fromWateringToWateringDTO(savedWatering);
    }

    public WateringEvent wateringEvent(WateringDTO wateringDTO) {
        WateringEvent wateringEvent = new WateringEvent();
        wateringEvent.setPlantId(wateringDTO.getPlantId());
        wateringEvent.setOwner(wateringDTO.getOwnerUsername());
        wateringEvent.setWateredAt(LocalDateTime.now());

        return wateringEvent;
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
