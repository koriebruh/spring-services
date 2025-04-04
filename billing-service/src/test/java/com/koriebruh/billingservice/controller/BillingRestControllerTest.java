package com.koriebruh.billingservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koriebruh.billingservice.dto.BillingRequestDTO;
import com.koriebruh.billingservice.dto.BillingResponseDTO;
import com.koriebruh.billingservice.dto.ErrorResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;  // Import yang benar

@SpringBootTest
@AutoConfigureMockMvc
class BillingRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(BillingRestControllerTest.class);


    @Test
    void testCreateBillingSuccess() throws Exception {
        BillingRequestDTO request = new BillingRequestDTO();
        request.setPatientId(1);
        request.setName("Nathyov");
        request.setEmail("Nathyow@gmail.com");

        mockMvc.perform(
                        post("/api/billing")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isCreated())
                .andDo(result -> {

                    BillingResponseDTO response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertEquals("ACTIVE", response.getStatus());
                });
    }


    @Test
    void testCreateBillingNotIncludeEmail() throws Exception {
        BillingRequestDTO request = new BillingRequestDTO();
        request.setPatientId(1);
        request.setName("Nathyov");

        mockMvc.perform(
                        post("/api/billing")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isBadRequest())
                .andDo(result -> {

                    ErrorResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertNotNull(response.getErrors());
                });
    }

    @Test
    void testCreateBillingNotIncludeID() throws Exception {
        BillingRequestDTO request = new BillingRequestDTO();
        request.setName("Nathyov");
        request.setEmail("Nathyow@gmail.com");


        mockMvc.perform(
                        post("/api/billing")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isBadRequest())
                .andDo(result -> {

                    ErrorResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    Assertions.assertNotNull(response.getErrors());
                });
    }
}
