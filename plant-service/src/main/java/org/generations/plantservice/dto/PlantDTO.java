package org.generations.plantservice.dto;

import lombok.*;

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

    private String lastWatered;
    private String ownerUsername;

}
