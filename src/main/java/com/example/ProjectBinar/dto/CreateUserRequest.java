package com.example.ProjectBinar.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest implements Serializable {

  private String username;
  private String email;
  private String password;
  private Boolean isActive;
}
