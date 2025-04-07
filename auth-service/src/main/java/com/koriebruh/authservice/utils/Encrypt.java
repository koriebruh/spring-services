package com.koriebruh.authservice.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Encrypt {

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public Encrypt(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String encryptPass(String password) {
        return passwordEncoder.encode(password);
    }

    public boolean matchesPass(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }
}