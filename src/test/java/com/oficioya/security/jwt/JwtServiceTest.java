package com.oficioya.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Set the properties that are normally injected by @Value
        ReflectionTestUtils.setField(jwtService, "secretKey", "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 86400000L);

        userDetails = User.builder()
                .username("testuser@oficioya.com")
                .password("password123")
                .roles("CONTRATANTE")
                .build();
    }

    @Test
    void testGenerateTokenAndExtractUsername() {
        // Act
        String token = jwtService.generateToken(userDetails);
        String extractedUsername = jwtService.extractUsername(token);

        // Assert
        assertNotNull(token);
        assertEquals(userDetails.getUsername(), extractedUsername);
    }

    @Test
    void testGenerateTokenWithExtraClaims() {
        // Arrange
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ROLE_TRABAJADOR");

        // Act
        String token = jwtService.generateToken(extraClaims, userDetails);
        String roleClaim = jwtService.extractClaim(token, claims -> claims.get("role", String.class));

        // Assert
        assertNotNull(token);
        assertEquals("ROLE_TRABAJADOR", roleClaim);
    }

    @Test
    void testIsTokenValid() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        // Act
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testIsTokenValidWithDifferentUser() {
        // Arrange
        String token = jwtService.generateToken(userDetails);

        UserDetails wrongUser = User.builder()
                .username("wronguser@oficioya.com")
                .password("password123")
                .roles("CONTRATANTE")
                .build();

        // Act
        boolean isValid = jwtService.isTokenValid(token, wrongUser);

        // Assert
        assertFalse(isValid);
    }
}
