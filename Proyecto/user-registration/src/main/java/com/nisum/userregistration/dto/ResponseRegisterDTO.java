package com.nisum.userregistration.dto;

import java.sql.Timestamp;
import java.util.UUID;

import lombok.Data;

@Data
public class ResponseRegisterDTO {

	private UUID id;
    private Timestamp created;
    private Timestamp modified;
    private Timestamp lastLogin;
    private String token;
    private Integer isActive;

}

