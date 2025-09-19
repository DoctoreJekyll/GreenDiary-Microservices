package org.generations.plantservice.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class  WateringDTO {
    private int id;
    private int plantId;
    private LocalDateTime waterTime;
}
