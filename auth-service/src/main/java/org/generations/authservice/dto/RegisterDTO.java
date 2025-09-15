package org.generations.authservice.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDTO {
    String username;
    String password;
}
