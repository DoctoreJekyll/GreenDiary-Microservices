package org.generations.plantservice.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "plants")
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String species;
    private String location;
    private String notes;

    @Column(name="last_watered")
    private LocalDateTime lastWatered;
    @Column(name="owner_username")
    private String ownerUsername;
}
