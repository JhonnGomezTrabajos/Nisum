package com.nisum.userregistration.model;

import java.sql.Timestamp;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "USERS")
public class User {
    
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "ID", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "NAME", nullable = false)
    private String name;
    
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;
    
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    
    @Column(name = "CREATED")
    private Timestamp created;
    
    @Column(name = "MODIFIED")
    private Timestamp modified;
    
    @Column(name = "LAST_LOGIN")
    private Timestamp lastLogin;
    
    @Column(name = "TOKEN")
    private String token;
    
    @Column(name = "IS_ACTIVE")
    private Integer isActive;
    
    @Embedded
    private Phone phone;

}
