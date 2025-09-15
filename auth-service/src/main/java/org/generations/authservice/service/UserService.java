package org.generations.authservice.service;


import org.generations.authservice.dto.RegisterDTO;
import org.generations.authservice.dto.UserDTO;
import org.generations.authservice.mapper.UserMapper;
import org.generations.authservice.model.UserApp;
import org.generations.authservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
  private final UserRepository repo;
  private final PasswordEncoder encoder;
  private final UserMapper mapper;

  public UserService(UserRepository repo, PasswordEncoder encoder, UserMapper mapper) {
    this.repo = repo;
    this.encoder = encoder;
    this.mapper = mapper;
  }

  @Transactional
  public UserApp register(RegisterDTO dto) {
    if (repo.findByUsername(dto.getUsername()).isPresent()) {
      throw new IllegalArgumentException("username exists");
    }

    UserApp u = mapper.toEntity(dto);
    u.setPassword(encoder.encode(dto.getPassword()));
    // guardamos roles como ROLE_USER por convención de Spring
    u.getRoles().add("ROLE_USER");

    return repo.save(u);
  }

  public UserDTO findByUsername(String username) {
    UserApp ua = repo.findByUsername(username)
            .orElseThrow(() -> new IllegalArgumentException("user not found"));

    return mapper.toDto(ua);
  }

  // devuelve entidad completa (necesario para generar token)
  public UserApp findEntityByUsername(String username) {
    return repo.findByUsername(username).
            orElseThrow(() -> new IllegalArgumentException("user not found"));
  }

  public List<UserDTO> findAll() {
    return repo.findAll().stream().map(mapper::toDto).toList();
  }

  public void deleteById(Long id) {
    repo.deleteById(id);
  }

}
