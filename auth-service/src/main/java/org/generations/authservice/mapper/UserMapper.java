package org.generations.authservice.mapper;

import org.generations.authservice.model.UserApp;
import org.generations.authservice.dto.RegisterDTO;
import org.generations.authservice.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // de RegisterDTO a UserApp
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", expression = "java(new java.util.HashSet<>())")
    @Mapping(target = "password", ignore = true)
    UserApp toEntity(RegisterDTO dto);

    // de UserApp a UserDTO
    UserDTO toDto(UserApp user);
}
