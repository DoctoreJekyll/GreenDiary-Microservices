package org.generations.plantservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.generations.plantservice.dto.PlantDTO;
import org.generations.plantservice.model.Plant;
import org.generations.plantservice.service.PlantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PlantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private PlantService plantService;

    @Test
    void shouldCreatePlantAndRetrieveIt() throws Exception {
        Plant plant = Plant.builder()
                .name("Helecho")
                .species("Nephrolepis exaltata")
                .build();

        // Crear
        mockMvc.perform(post("/api/plants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(plant)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Helecho"));

        // Listar
        mockMvc.perform(get("/api/plants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Helecho"));
    }

    @Test
    void shouldReturnPlantWhenExist() throws Exception {
        PlantDTO plantDTO = PlantDTO.builder().id(2).name("Rosa").build();

        //Mock
        when(plantService.findById(2)).thenReturn(Optional.of(plantDTO));

        //Ejecutamos
        mockMvc.perform(get("/api/plants/2")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Rosa"));
    }


    @Test
    void shouldReturn404WhenPlantNotFound() throws Exception {
        // Simulamos que el service devuelve vacío
        when(plantService.findById(98)).thenReturn(Optional.empty());

        // Ejecutamos GET y verificamos que devuelve error 404 con nuestro JSON de error
        mockMvc.perform(get("api/plants/98")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Plant not found 98"));
    }
}
