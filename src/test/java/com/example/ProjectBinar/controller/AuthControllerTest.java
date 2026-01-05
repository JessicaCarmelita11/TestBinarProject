package com.example.ProjectBinar.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.ProjectBinar.dto.AuthRequest;
import com.example.ProjectBinar.dto.AuthResponse;
import com.example.ProjectBinar.service.AuthService;
import com.example.ProjectBinar.service.PasswordResetService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Unit Tests")
class AuthControllerTest {

  @Mock private AuthService authService;

  @Mock private PasswordResetService passwordResetService;

  @InjectMocks private AuthController authController;

  private AuthRequest validRequest;
  private AuthResponse validResponse;

  @BeforeEach
  void setUp() {
    validRequest = AuthRequest.builder().username("testuser").password("password123").build();

    validResponse =
        AuthResponse.builder()
            .token("jwt.token.here")
            .type("Bearer")
            .username("testuser")
            .email("test@example.com")
            .roles(List.of("CUSTOMER"))
            .expiresIn(86400000L)
            .build();
  }

  @Test
  @DisplayName("POST /auth/login - Success with valid credentials")
  void login_WithValidCredentials_ShouldReturnToken() {
    // Arrange
    when(authService.authenticate(any(AuthRequest.class))).thenReturn(validResponse);

    // Act
    ResponseEntity<?> response = authController.login(validRequest);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    AuthResponse body = (AuthResponse) response.getBody();
    assertEquals("jwt.token.here", body.getToken());
    assertEquals("Bearer", body.getType());
    assertEquals("testuser", body.getUsername());
    assertEquals("test@example.com", body.getEmail());
    assertTrue(body.getRoles().contains("CUSTOMER"));

    verify(authService).authenticate(any(AuthRequest.class));
  }

  @Test
  @DisplayName("POST /auth/login - Fail with invalid credentials")
  @SuppressWarnings("unchecked")
  void login_WithInvalidCredentials_ShouldReturn401() {
    // Arrange
    when(authService.authenticate(any(AuthRequest.class)))
        .thenThrow(new BadCredentialsException("Invalid credentials"));

    // Act
    ResponseEntity<?> response = authController.login(validRequest);

    // Assert
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNotNull(response.getBody());
    Map<String, String> body = (Map<String, String>) response.getBody();
    assertEquals("Authentication failed", body.get("error"));
    assertEquals("Invalid username or password", body.get("message"));
  }

  @Test
  @DisplayName("GET /auth/test - Should return success message")
  void test_ShouldReturnSuccessMessage() {
    // Act
    ResponseEntity<String> response = authController.test();

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Auth endpoint is working!", response.getBody());
  }

  @Test
  @DisplayName("GET /auth/validate-token - Valid token")
  @SuppressWarnings("unchecked")
  void validateToken_WithValidToken_ShouldReturnTrue() {
    // Arrange
    when(passwordResetService.validateToken("VALID123")).thenReturn(true);

    // Act
    ResponseEntity<?> response = authController.validateToken("VALID123");

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Map<String, Boolean> body = (Map<String, Boolean>) response.getBody();
    assertTrue(body.get("valid"));
  }

  @Test
  @DisplayName("GET /auth/validate-token - Invalid token")
  @SuppressWarnings("unchecked")
  void validateToken_WithInvalidToken_ShouldReturnFalse() {
    // Arrange
    when(passwordResetService.validateToken("INVALID123")).thenReturn(false);

    // Act
    ResponseEntity<?> response = authController.validateToken("INVALID123");

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Map<String, Boolean> body = (Map<String, Boolean>) response.getBody();
    assertFalse(body.get("valid"));
  }
}
