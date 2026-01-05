package com.example.ProjectBinar.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Auth Response DTO - Response untuk login yang berhasil.
 *
 * <p>Berisi JWT token dan informasi user.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
  private String token;
  private String type;
  private String username;
  private String email;
  private List<String> roles;
  private Long expiresIn;
}
