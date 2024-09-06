package com.nisum.userregistration.service;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nisum.userregistration.dto.PhoneDTO;
import com.nisum.userregistration.dto.ResponseRegisterDTO;
import com.nisum.userregistration.dto.UserDTO;
import com.nisum.userregistration.model.Phone;
import com.nisum.userregistration.model.User;
import com.nisum.userregistration.repository.UserRepository;
import com.nisum.userregistration.util.JwtUtils;

@Service
public class UserServiceImpl implements UserService {
	
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    
    @Value("${email.validation.regex}")
    private String emailRegex;
    
    @Value("${password.regex}")
    private String passwordRegex;
    
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	

	@Autowired
	private JwtUtils jwtUtil;

	public ResponseRegisterDTO registerUser(UserDTO userDTO) {
        logger.info("[UserServiceImpl][registerUser] Ingresando con datos: {}", userDTO);
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            logger.error("[UserServiceImpl][registerUser] El correo {} ya está registrado", userDTO.getEmail());
            throw new RuntimeException("El correo ya registrado");
        }

        validateEmail(userDTO.getEmail());
        validatePassword(userDTO.getPassword());

        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setCreated(new Timestamp(System.currentTimeMillis()));
        user.setModified(new Timestamp(System.currentTimeMillis()));
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));
        user.setIsActive(1);
        user.setPhone(mapPhoneDTO(userDTO.getPhones().get(0)));

        String token = jwtUtil.generateToken(user.getName(),user.getEmail());
        logger.info("[UserServiceImpl][registerUser] TOKEN generado= "+token);
        user.setToken(token);
        logger.info("[UserServiceImpl][registerUser] Va a guardar data a la BD= "+user);
        User savedUser = userRepository.save(user);
        logger.info("[UserServiceImpl][registerUser] Usuario registrado exitosamente: {}", savedUser);
        ResponseRegisterDTO response = new ResponseRegisterDTO();
        response.setId(savedUser.getId());
        response.setCreated(savedUser.getCreated());
        response.setModified(savedUser.getModified());
        response.setLastLogin(savedUser.getLastLogin());
        response.setToken(savedUser.getToken());
        response.setIsActive(savedUser.getIsActive());
        return response;
    }

    private Phone mapPhoneDTO(PhoneDTO phoneDTO) {
        logger.info("[UserServiceImpl][mapPhoneDTO] Mapeando PhoneDTO a Phone: {}", phoneDTO);
        Phone phone = new Phone();
        phone.setPhoneNumber(phoneDTO.getNumber());
        phone.setPhoneCityCode(phoneDTO.getCitycode());
        phone.setPhoneCountryCode(phoneDTO.getContrycode());
        logger.info("[UserServiceImpl][mapPhoneDTO] Phone mapeado: {}", phone);
        return phone;
    }

    public void validateEmail(String email) {
        logger.info("[UserServiceImpl][validateEmail] Validando email: {}", email);
        
        if (email == null || !email.matches(emailRegex)) {
            logger.error("[UserServiceImpl][validateEmail] Validación fallida para el email: {}", email);
            throw new IllegalArgumentException("Formato de email inválido. Debe seguir el formato aaaaaaa@dominio.cl");
        }
        
        logger.info("[UserServiceImpl][validateEmail] Validación de email exitosa para: {}", email);
    }

    public void validatePassword(String password) {
        logger.info("[UserServiceImpl][validatePassword] Validando password con expresión regular: {}", passwordRegex);

        if (password == null || !password.matches(passwordRegex)) {
            String errorMessage = "La contraseña debe cumplir con los siguientes requisitos: "
                    + "                                                            "
                    + "- Debe contener al menos una letra (mayúscula o minúscula).   "
                    + "                                                                      "
                    + "- Debe contener al menos un dígito (del 0 al 9).     "
                    + "                                                                               "
                    + "- Debe tener una longitud mínima de 8 caracteres.    "
                    + "                                                                              "
                    + " - Debe tener minimo un caracter especial.";
            logger.error("[UserServiceImpl][validatePassword] Validación fallida para la contraseña: {}. {}", password, errorMessage);
            logger.error("[UserServiceImpl][validatePassword] Validación fallida para el password: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        logger.info("[UserServiceImpl][validatePassword] Validación de password exitosa");
    }
}
