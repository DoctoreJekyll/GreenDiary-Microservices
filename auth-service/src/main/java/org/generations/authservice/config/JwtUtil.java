package org.generations.authservice.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Set;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration-ms}")
  private long expirationMs;

  public String generateToken(String username, Set<String> roles) {
    var key = Keys.hmacShaKeyFor(secret.getBytes());
    return Jwts.builder()
      .setSubject(username)
      .claim("roles", roles)
      .setIssuedAt(new Date())
      .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }
}
