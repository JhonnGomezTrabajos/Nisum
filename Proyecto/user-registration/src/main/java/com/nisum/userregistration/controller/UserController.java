package com.nisum.userregistration.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nisum.userregistration.dto.ResponseRegisterDTO;
import com.nisum.userregistration.dto.UserDTO;
import com.nisum.userregistration.exception.ErrorResponse;
import com.nisum.userregistration.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user with the provided details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User successfully registered", 
            content = @Content(schema = @Schema(implementation = ResponseRegisterDTO.class))),
        @ApiResponse(responseCode = "400", description = "Bad request due to validation or other errors",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDto) {
        logger.info("[UserController][registerUser] Ingresando solicitud de registro con datos: {}", userDto);
        try {
            ResponseRegisterDTO newUser = userService.registerUser(userDto);
            logger.info("[UserController][registerUser] Usuario registrado exitosamente: {}", newUser);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("[UserController][registerUser] Error al registrar usuario: {}", e.getMessage(), e);
            return new ResponseEntity<>(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Error, " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
