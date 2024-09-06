package com.nisum.userregistration.service;

import com.nisum.userregistration.dto.ResponseRegisterDTO;
import com.nisum.userregistration.dto.UserDTO;

public interface UserService {

    public ResponseRegisterDTO registerUser(UserDTO userDto);
}
