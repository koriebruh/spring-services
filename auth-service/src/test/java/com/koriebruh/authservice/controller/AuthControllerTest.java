package com.koriebruh.authservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koriebruh.authservice.dto.*;
import com.koriebruh.authservice.repository.UserRepository;
import com.koriebruh.authservice.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    private static final Logger log = LoggerFactory.getLogger(AuthControllerTest.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testusers");
        request.setPassword("testusers");
        request.setEmail("testusers@gmail.com");

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isCreated())
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertEquals("CREATED", response.getStatus());
                });
    }

    @Test
    void testCreateUserErrorForgotEmail() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testusers");
        request.setPassword("testusers");

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isBadRequest())
                .andDo(result -> {
                    ErrorResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertNotNull(response.getStatus());
                });
    }

    @Test
    void testCreateUserErrorConflict() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testusers");
        request.setPassword("testusers");
        request.setEmail("testusers@gmail.com");
        authService.registerUser(request);

        RegisterRequest requestConflict = new RegisterRequest();
        requestConflict.setUsername(request.getUsername());
        requestConflict.setPassword(request.getPassword());
        requestConflict.setEmail(request.getEmail());

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestConflict))
                ).andExpect(status().isConflict())
                .andDo(result -> {
                    ErrorResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertNotNull(response.getStatus());
                });
    }


    @Test
    void testLoginUserSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testusers");
        request.setPassword("testusers");
        request.setEmail("testusers@gmail.com");
        authService.registerUser(request);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(request.getUsername());
        loginRequest.setPassword(request.getPassword());


        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                ).andExpect(status().isOk())
                .andDo(result -> {
                    WebResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertEquals("OK", response.getStatus());
                });
    }

    @Test
    void testLoginUserFail() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testusers");
        request.setPassword("testusers");
        request.setEmail("testusers@gmail.com");
        authService.registerUser(request);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(request.getUsername());
        loginRequest.setPassword("wrongpassword");


        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                ).andExpect(status().isBadRequest())
                .andDo(result -> {
                    ErrorResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertNotNull(response.getStatus());
                    Assertions.assertEquals("ERROR", response.getStatus());
                });
    }

    @Test
    void testValidateToken() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testusers");
        request.setPassword("testusers");
        request.setEmail("testusers@gmail.com");
        authService.registerUser(request);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(request.getUsername());
        loginRequest.setPassword("testusers");

        //LOGIN USER
        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest))
                ).andExpect(status().isOk())
                .andDo(result -> {
                    WebResponse<LoginResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertEquals("OK", response.getStatus());

                    // DO VALIDATE TOKEN
                    mockMvc.perform(
                                    get("/auth/validate")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .accept(MediaType.APPLICATION_JSON)
                                            .header("Authorization", "Bearer " + response.getData().getRefreshToken())
                            ).andExpect(status().isOk())
                            .andDo(resultValidate -> {
                                WebResponse<String> responseValidate = objectMapper.readValue(resultValidate.getResponse().getContentAsString(), new TypeReference<>() {
                                });
                                /*NO CONTENT IN RESPONSE
                                 * */
                                Assertions.assertEquals("OK", responseValidate.getStatus());
                                Assertions.assertNotNull(responseValidate.getData());
                            });
                });
    }


}