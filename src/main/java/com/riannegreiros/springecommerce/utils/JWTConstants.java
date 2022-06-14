package com.riannegreiros.springecommerce.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTConstants {
    public static String JWTSECRET;

    @Value("${jwt.secret}")
    public void setJwtsecret(String jwtsecret) {
        JWTSECRET = jwtsecret;
    }
}
