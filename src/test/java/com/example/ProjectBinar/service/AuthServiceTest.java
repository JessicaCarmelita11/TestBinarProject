package com.example.ProjectBinar.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.ProjectBinar.dto.AuthRequest;
import com.example.ProjectBinar.dto.AuthResponse;
import com.example.ProjectBinar.entity.Role;
import com.example.ProjectBinar.entity.User;
import com.example.ProjectBinar.security.CustomUserDetails;
import com.example.ProjectBinar.security.JwtService;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Unit Tests")
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private CustomUserDetails userDetails;
    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        Role customerRole = Role.builder().id(1L).name("CUSTOMER").build();

        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("hashedPassword")
                .isActive(true)
                .roles(Set.of(customerRole))
                .build();

        userDetails = new CustomUserDetails(testUser);

        authRequest = AuthRequest.builder().username("testuser").password("password123").build();
    }

    @Test
    @DisplayName("Should authenticate successfully with valid credentials")
    void authenticate_WithValidCredentials_ShouldReturnAuthResponse() {
        // Arrange
        String expectedToken = "jwt.token.here";
        long expectedExpiration = 86400000L;

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn(expectedToken);
        when(jwtService.getExpirationTime()).thenReturn(expectedExpiration);

        // Act
        AuthResponse response = authService.authenticate(authRequest);

        // Assert
        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals(1, response.getRoles().size());
        assertTrue(response.getRoles().contains("CUSTOMER"));
        assertEquals(expectedExpiration, response.getExpiresIn());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(userDetails);
    }

    @Test
    @DisplayName("Should throw exception with invalid credentials")
    void authenticate_WithInvalidCredentials_ShouldThrowException() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.authenticate(authRequest));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    @DisplayName("Should include all roles in response for user with multiple roles")
    void authenticate_WithMultipleRoles_ShouldReturnAllRoles() {
        // Arrange
        Role adminRole = Role.builder().id(2L).name("ADMIN").build();
        Role marketingRole = Role.builder().id(3L).name("MARKETING").build();

        User multiRoleUser = User.builder()
                .id(2L)
                .username("admin")
                .email("admin@example.com")
                .password("hashedPassword")
                .isActive(true)
                .roles(Set.of(adminRole, marketingRole))
                .build();

        CustomUserDetails multiRoleUserDetails = new CustomUserDetails(multiRoleUser);
        String expectedToken = "admin.jwt.token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(multiRoleUserDetails);
        when(jwtService.generateToken(multiRoleUserDetails)).thenReturn(expectedToken);
        when(jwtService.getExpirationTime()).thenReturn(86400000L);

        AuthRequest adminRequest = AuthRequest.builder().username("admin").password("password").build();

        // Act
        AuthResponse response = authService.authenticate(adminRequest);

        // Assert
        assertNotNull(response);
        assertEquals(2, response.getRoles().size());
        assertTrue(response.getRoles().contains("ADMIN"));
        assertTrue(response.getRoles().contains("MARKETING"));
    }
}
