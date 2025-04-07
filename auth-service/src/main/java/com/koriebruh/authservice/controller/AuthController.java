package com.koriebruh.authservice.controller;

import com.koriebruh.authservice.dto.*;
import com.koriebruh.authservice.service.AuthService;
import com.koriebruh.authservice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth/")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<LoginResponse>> loginUser(@RequestBody LoginRequest request) {
        LoginResponse response = authService.loginUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                WebResponse.<LoginResponse>builder()
                        .status("OK")
                        .data(response)
                        .build()
        );
    }

    @PostMapping(value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<String>> registerUser(@RequestBody RegisterRequest request) {
        String msg = authService.registerUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                WebResponse.<String>builder()
                        .status("CREATED")
                        .data(msg)
                        .build()
        );
    }

    @GetMapping(value = "/validate",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WebResponse<String>> validatedToken(@RequestHeader("Authorization") String authHeader) {
        /* Check if the Authorization header is present and starts with "Bearer "
        *Authorization: Bearer <token>
        */
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Authorization header is missing or invalid");
        }

        String token = authHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        if (!jwtUtil.validateToken(token, username)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Token is invalid");
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                WebResponse.<String>builder()
                        .status("OK")
                        .data("Token is valid")
                        .build()
        );
    }
}

