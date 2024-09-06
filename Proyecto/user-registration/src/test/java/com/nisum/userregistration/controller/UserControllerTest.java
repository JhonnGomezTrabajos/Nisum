package com.nisum.userregistration.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nisum.userregistration.dto.ResponseRegisterDTO;
import com.nisum.userregistration.dto.UserDTO;
import com.nisum.userregistration.exception.ErrorResponse;
import com.nisum.userregistration.service.UserService;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private Logger logger;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        UserDTO userDto = new UserDTO();
        UUID uuid = UUID.randomUUID();
        ResponseRegisterDTO responseRegisterDTO = new ResponseRegisterDTO();
        responseRegisterDTO.setId(uuid);
        responseRegisterDTO.setToken("dummyToken");

        when(userService.registerUser(any(UserDTO.class))).thenReturn(responseRegisterDTO);

        ResponseEntity<?> response = userController.registerUser(userDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseRegisterDTO, response.getBody());
    }

    @Test
    public void testRegisterUser_Error() {
        UserDTO userDto = new UserDTO();
        RuntimeException runtimeException = new RuntimeException("Test exception");

        when(userService.registerUser(any(UserDTO.class))).thenThrow(runtimeException);

        ResponseEntity<?> response = userController.registerUser(userDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatusCode());
        assertEquals("Error, Test exception", errorResponse.getMensaje());
    }

    @Test
    public void testRegisterUser_Logging() {
        UUID uuid2 = UUID.randomUUID();
        UserDTO userDto = new UserDTO();
        ResponseRegisterDTO responseRegisterDTO = new ResponseRegisterDTO();
        responseRegisterDTO.setId(uuid2);
        responseRegisterDTO.setToken("dummyToken");

        when(userService.registerUser(any(UserDTO.class))).thenReturn(responseRegisterDTO);

        ResponseEntity<?> response = userController.registerUser(userDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseRegisterDTO, response.getBody());
    }

}
