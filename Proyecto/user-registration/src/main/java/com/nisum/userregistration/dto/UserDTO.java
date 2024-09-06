package com.nisum.userregistration.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserDTO {

    private String name;
    private String email;
    private String password;
    private List<PhoneDTO> phones;

}

