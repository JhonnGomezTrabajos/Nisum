package com.nisum.userregistration.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nisum.userregistration.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	boolean existsByEmail(String email); 
    Optional<User> findByEmail(String email);
}
