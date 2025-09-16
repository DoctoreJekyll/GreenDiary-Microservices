package org.generations.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDTO {
    @NotBlank @Size(min = 4, max = 20)
    String username;
    @NotBlank @Size(min = 8, max = 30)
    String password;
}
