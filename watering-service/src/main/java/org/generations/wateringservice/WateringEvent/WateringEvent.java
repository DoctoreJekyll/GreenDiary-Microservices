package org.generations.wateringservice.WateringEvent;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WateringEvent {
    private int plantId;
    private String owner;
    private LocalDateTime wateredAt;
    private String eventType = "PLANT_WATERED";
}
