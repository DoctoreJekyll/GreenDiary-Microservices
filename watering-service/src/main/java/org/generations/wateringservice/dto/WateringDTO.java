package org.generations.wateringservice.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WateringDTO {
    private Integer id;
    private Integer plantId;
    private Date wateringDate;
    private String notes;


}
