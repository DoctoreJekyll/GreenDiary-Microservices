package org.generations.plantservice.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PlantWithWateringDTO{
    private PlantDTO plantDTO;
    private WateringDTO wateringDTO;
}