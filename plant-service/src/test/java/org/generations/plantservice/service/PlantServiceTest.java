package org.generations.plantservice.service;

import org.generations.plantservice.dto.PlantDTO;
import org.generations.plantservice.mapper.PlantMapper;
import org.generations.plantservice.model.Plant;
import org.generations.plantservice.repository.PlantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PlantServiceTest {

    private PlantRepository repo;
    private PlantMapper mapper;
    private PlantService service;

    @BeforeEach
    void setUp() {
        repo = mock(PlantRepository.class);         // fake repo
        mapper = new PlantMapper();                 // mapper real
        service = new PlantService(repo, mapper);   // service con repo fake + mapper real
    }

    @Test
    void create_shouldSavePlant() {
        // DTO de prueba
        PlantDTO plantDTO = PlantDTO.builder()
                .name("Cactus")
                .species("Echinopsis")
                .location("Terraza")
                .notes("Poco riego")
                .build();

        // Simulamos lo que devuelve repo.save()
        Plant plantEntity = mapper.toPlant(plantDTO);
        plantEntity.setId(99); // simular ID asignado
        when(repo.save(any(Plant.class))).thenReturn(plantEntity);

        PlantDTO savedDTO = service.createPlantDTO(plantDTO);

        // Verificamos que repo.save se llamó con la entidad correcta
        ArgumentCaptor<Plant> captor = ArgumentCaptor.forClass(Plant.class);
        verify(repo).save(captor.capture());

        Plant saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("Cactus");
        assertThat(saved.getSpecies()).isEqualTo("Echinopsis");

        // Verificamos también el DTO devuelto
        assertThat(savedDTO.getId()).isEqualTo(99);
        assertThat(savedDTO.getName()).isEqualTo("Cactus");
    }

    @Test
    void getAll_shouldReturnList() {
        when(repo.findAll()).thenReturn(List.of(
                Plant.builder().id(1).name("Aloe").species("Aloe Vera").build()
        ));

        List<PlantDTO> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Aloe");
        assertThat(result.get(0).getSpecies()).isEqualTo("Aloe Vera");
    }

    @Test
    void getById_shouldReturnPlantIfExists() {
        Plant plant = Plant.builder().id(2).name("Ficus").species("Ficus Benjamina").build();
        when(repo.findById(2)).thenReturn(Optional.of(plant));

        Optional<PlantDTO> result = service.findById(2);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Ficus");
        assertThat(result.get().getSpecies()).isEqualTo("Ficus Benjamina");
    }
}
