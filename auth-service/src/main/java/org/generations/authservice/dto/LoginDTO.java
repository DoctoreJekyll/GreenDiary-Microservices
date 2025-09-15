package org.generations.authservice.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO {
    String username;
    String password;
}
