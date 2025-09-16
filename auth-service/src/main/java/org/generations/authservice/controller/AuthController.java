package org.generations.authservice.controller;

import org.generations.authservice.config.JwtUtil;
import org.generations.authservice.dto.AuthResponseDTO;
import org.generations.authservice.dto.LoginDTO;
import org.generations.authservice.dto.RegisterDTO;
import org.generations.authservice.dto.UserDTO;
import org.generations.authservice.model.UserApp;
import org.generations.authservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final UserService userService;
  private final AuthenticationManager authManager;
  private final JwtUtil jwtUtil;

  public AuthController(UserService userService, AuthenticationManager authManager, JwtUtil jwtUtil){
    this.userService = userService; this.authManager = authManager; this.jwtUtil = jwtUtil;
  }

  @PostMapping("/register")
  public ResponseEntity<UserDTO> register(@RequestBody RegisterDTO dto){
    UserApp saved = userService.register(dto);
    return ResponseEntity.ok(userService.findByUsername(saved.getUsername()));
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDTO dto) {
    // autenticar username/password
    Authentication auth = authManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
    );
    SecurityContextHolder.getContext().setAuthentication(auth);

    // obtener el usuario real
    UserApp user = userService.findEntityByUsername(dto.getUsername());

    // generar token
    String token = jwtUtil.generateToken(user.getUsername(), user.getRoles());

    // convertir a DTO para devolver
    UserDTO userDTO = userService.findByUsername(user.getUsername());

    return ResponseEntity.ok(new AuthResponseDTO(token, userDTO));
  }

  @GetMapping("/me")
  public ResponseEntity<UserDTO> me(Authentication authentication) {
    String username = authentication.getName();
    UserDTO dto = userService.findByUsername(username);
    return ResponseEntity.ok(dto);
  }


  //ADMIN ACTIONS
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<List<UserDTO>> listAll() {
    return ResponseEntity.ok(userService.findAll());
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

}
