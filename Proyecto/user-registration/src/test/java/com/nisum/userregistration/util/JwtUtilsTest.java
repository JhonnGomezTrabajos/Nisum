package com.nisum.userregistration.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import io.jsonwebtoken.Claims;

@SpringBootTest
public class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    private static final String DUMMY_SECRET_KEY = "myverylongsecretkeymyverylongsecretkeymyverylongsecretkey";
    private static final long DUMMY_EXPIRATION = 3600000L; //1-hora

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        setField(jwtUtils, "secretKey", DUMMY_SECRET_KEY);
        setField(jwtUtils, "expiration", DUMMY_EXPIRATION);
    }

    @Test
    public void testGenerateToken() {
        String username = "testUser";
        String email = "test@example.com";
        String token = jwtUtils.generateToken(username, email);
        Claims claims = jwtUtils.getClaimsFromToken(token);
        assertEquals(username, claims.getSubject());
        assertEquals(email, claims.get("email"));
    }

    @Test
    public void testGetUsernameFromToken() {
        String username = "testUser";
        String email = "test@example.com";
        String token = jwtUtils.generateToken(username, email);
        String extractedUsername = jwtUtils.getUsernameFromToken(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    public void testGetEmailFromToken() {
        String username = "testUser";
        String email = "test@example.com";
        String token = jwtUtils.generateToken(username, email);
        String extractedEmail = jwtUtils.getEmailFromToken(token);
        assertEquals(email, extractedEmail);
    }

    @Test
    public void testValidateToken_ValidToken() {
        String username = "testUser";
        String email = "test@example.com";
        String token = jwtUtils.generateToken(username, email);
        boolean isValid = jwtUtils.validateToken(token);
        assertTrue(isValid);
    }

    @Test
    public void testValidateToken_InvalidToken() {
        String invalidToken = "invalidToken";
        boolean isValid = jwtUtils.validateToken(invalidToken);
        assertFalse(isValid);
    }

    private void setField(Object obj, String fieldName, Object value) {
        try {
            var field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
