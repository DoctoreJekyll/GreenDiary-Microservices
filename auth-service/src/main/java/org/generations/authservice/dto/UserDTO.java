package org.generations.authservice.dto;


import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private int id;
    private String username;
    private Set<String> roles;
}
