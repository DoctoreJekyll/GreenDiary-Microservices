package org.generations.plantservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantDTO {
    private Integer id;

    private String name;
    private String species;
    private String location;
    private String notes;

    private LocalDateTime lastWatered;
    private String ownerUsername;

}
