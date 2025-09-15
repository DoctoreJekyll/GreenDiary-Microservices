package org.generations.authservice.config;


import org.generations.authservice.model.UserApp;
import org.generations.authservice.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {
  private final UserRepository repo;
  public JpaUserDetailsService(UserRepository repo) { this.repo = repo; }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserApp u = repo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("no"));
    return org.springframework.security.core.userdetails.User
      .withUsername(u.getUsername())
      .password(u.getPassword())
      .authorities(u.getRoles().toArray(new String[0]))
      .accountExpired(false).accountLocked(false).credentialsExpired(false).disabled(false)
      .build();
  }
}
