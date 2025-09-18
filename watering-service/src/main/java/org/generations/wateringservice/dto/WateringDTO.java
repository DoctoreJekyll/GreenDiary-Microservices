package org.generations.wateringservice.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WateringDTO {
    private Integer id;
    private Integer plantId;
    private LocalDateTime wateringDate;
    private String notes;

    private String ownerUsername;
}
