package org.generations.plantservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WateringEvent {
    private int plantId;
    private String owner;
    private LocalDateTime wateredAt;
    private String eventType = "PLANT_WATERED";
}
