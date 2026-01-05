package com.example.ProjectBinar.security;

import static org.junit.jupiter.api.Assertions.*;

import com.example.ProjectBinar.entity.Role;
import com.example.ProjectBinar.entity.User;
import java.lang.reflect.Field;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("JwtService Unit Tests")
class JwtServiceTest {

    private JwtService jwtService;
    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();

        // Set secret key using reflection (since it's @Value injected)
        Field secretKeyField = JwtService.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        // Base64 encoded secret key (at least 256 bits for HS256)
        secretKeyField.set(
                jwtService,
                "dGhpc19pc19hX3ZlcnlfdmVyeV9sb25nX3NlY3JldF9rZXlfZm9yX2p3dF90b2tlbl9nZW5lcmF0aW9u");

        // Set expiration time
        Field expirationField = JwtService.class.getDeclaredField("jwtExpiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtService, 86400000L); // 24 hours

        // Create test user
        Role customerRole = Role.builder().id(1L).name("CUSTOMER").build();
        User testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("hashedPassword")
                .isActive(true)
                .roles(Set.of(customerRole))
                .build();

        userDetails = new CustomUserDetails(testUser);
    }

    @Test
    @DisplayName("Should generate valid JWT token")
    void generateToken_ShouldReturnValidToken() {
        // Act
        String token = jwtService.generateToken(userDetails);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }

    @Test
    @DisplayName("Should extract username from token")
    void extractUsername_ShouldReturnCorrectUsername() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals("testuser", username);
    }

    @Test
    @DisplayName("Should validate token successfully")
    void isTokenValid_WithValidToken_ShouldReturnTrue() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false for token with wrong username")
    void isTokenValid_WithDifferentUser_ShouldReturnFalse() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        Role otherRole = Role.builder().id(2L).name("ADMIN").build();
        User otherUser = User.builder()
                .id(2L)
                .username("otheruser")
                .email("other@example.com")
                .password("password")
                .isActive(true)
                .roles(Set.of(otherRole))
                .build();
        CustomUserDetails otherUserDetails = new CustomUserDetails(otherUser);

        // Act
        boolean isValid = jwtService.isTokenValid(token, otherUserDetails);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return correct expiration time")
    void getExpirationTime_ShouldReturnConfiguredValue() {
        // Act
        long expirationTime = jwtService.getExpirationTime();

        // Assert
        assertEquals(86400000L, expirationTime);
    }
}
