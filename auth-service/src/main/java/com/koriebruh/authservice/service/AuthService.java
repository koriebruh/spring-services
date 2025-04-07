package com.koriebruh.authservice.service;


import com.koriebruh.authservice.dto.LoginRequest;
import com.koriebruh.authservice.dto.LoginResponse;
import com.koriebruh.authservice.dto.RegisterRequest;
import com.koriebruh.authservice.entity.User;
import com.koriebruh.authservice.repository.UserRepository;
import com.koriebruh.authservice.utils.Encrypt;
import com.koriebruh.authservice.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Optional;


@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private Encrypt encrypt;

    @Autowired
    private JwtUtil jwtUtil;

    public String registerUser(RegisterRequest request) {
        validationService.validate(request);

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username address already in use");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email address already in use");
        }

        //ENCRYPT PASSWORD
        String encryptedPass = encrypt.encryptPass(request.getPassword());

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPassword(encryptedPass);
        newUser.setCreatedAt(Instant.now().getEpochSecond());

        userRepository.save(newUser);
        return "User registered successfully";
    }

    public LoginResponse loginUser(LoginRequest request) {
        Optional<User> userOps = userRepository.findByUsernameAndDeletedAtIsNull(request.getUsername());
        if (userOps.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or Password wrong");
        }

        User user = userOps.get();

        /* Validated password
        * */
        if (!encrypt.matchesPass(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or Password wrong");
        }

        /* if old token already valid return old token
        *  then if token expired or null generate new token
        * */
        String refreshToken;
        if (user.getRefreshToken() != null && jwtUtil.validateToken(user.getRefreshToken(), user.getUsername())) {
            refreshToken = user.getRefreshToken();
        } else {
            // generate new access token
            refreshToken = jwtUtil.generateToken(user.getUsername(), 86400000L);
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
        }

        // Access token always generated fresh
        String accessToken = jwtUtil.generateToken(user.getUsername(), 900000L);

        LoginResponse result = new LoginResponse();
        result.setAccessToken(accessToken);
        result.setRefreshToken(refreshToken);
        result.setTokenType("Bearer");
        return result;
    }
}
