package com.nisum.userregistration.util;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}") 
    String secretKey;

    @Value("${jwt.expiration}")
    long expiration;

    private Key getSigningKey() {
        return new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(String username, String email) {
        logger.info("[JwtUtils][generateToken] Generando token para usuario: {}", username);
        String token = Jwts.builder()
                .setSubject(username)
                .claim("email", email)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
        logger.info("[JwtUtils][generateToken] Token generado: {}", token);
        return token;
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        logger.info("[JwtUtils][getUsernameFromToken] Extrayendo nombre de usuario del token");
        return getClaimsFromToken(token).getSubject();
    }

    public String getEmailFromToken(String token) {
        logger.info("[JwtUtils][getEmailFromToken] Extrayendo email del token");
        return getClaimsFromToken(token).get("email", String.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            logger.info("[JwtUtils][validateToken] Token válido");
            return true;
        } catch (Exception e) {
            logger.error("[JwtUtils][validateToken] Token inválido: {}", e.getMessage());
            return false;
        }
    }
}
