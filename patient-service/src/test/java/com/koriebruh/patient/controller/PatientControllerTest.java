package com.koriebruh.patient.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.koriebruh.patient.dto.ErrorResponse;
import com.koriebruh.patient.dto.PatientRequest;
import com.koriebruh.patient.dto.PatientResponse;
import com.koriebruh.patient.dto.WebResponse;
import com.koriebruh.patient.repository.PatientRepository;
import com.koriebruh.patient.service.PatientService;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class PatientControllerTest {

    private static final Logger log = LoggerFactory.getLogger(PatientControllerTest.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientRepository patientRepository;

    private ObjectMapper objectMapper;
    @Autowired
    private PatientService patientService;

    private static final Logger logger = LoggerFactory.getLogger(PatientControllerTest.class);

    @BeforeEach
    void setUp() {
        patientRepository.deleteAll();
        objectMapper = new ObjectMapper();
    }

    /*
     * TEST CASE FOR CREATE
     * - testCreatePatientSuccess
     * - testCreatePatientDuplicateEmail
     * - testCreatePatientMissingData
     * */
    @Test
    void testCreatePatientSuccess() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setName("testttt");
        request.setAddress("jalan baru jadi");
        request.setDateOfBirth("12-21-1231");
        request.setGender("MAN");
        request.setEmail("test@test.com");

        mockMvc.perform(
                post("/api/patients")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            Assertions.assertEquals("CREATED", response.getStatus());
        });
    }

    @Test
    void testCreatePatientDuplicateEmail() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setName("testttt");
        request.setAddress("jalan baru jadi");
        request.setDateOfBirth("12-21-1231");
        request.setGender("MAN");
        request.setEmail("test@test.com");

        mockMvc.perform(
                post("/api/patients")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isCreated()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            Assertions.assertEquals("CREATED", response.getStatus());
        });

        //CREATE ANOTHER REQUEST SAME EMAIL
        PatientRequest request2 = new PatientRequest();
        request2.setName("testttt2");
        request2.setAddress("jalan baru jadi 2");
        request2.setDateOfBirth("12-21-1232");
        request2.setGender("MAN");
        request2.setEmail("test@test.com");

        mockMvc.perform(
                post("/api/patients")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isConflict()
        ).andDo(result -> {
            ErrorResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            Assertions.assertNotNull(response.getErrors());
        });


    }

    @Test
    void testCreatePatientMissingData() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setName("testttt");
        request.setAddress("jalan baru jadi");

        mockMvc.perform(
                post("/api/patients")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isBadRequest()
        ).andDo(result -> {
            ErrorResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            Assertions.assertNotNull(response.getErrors());
        });
    }

    /*
     * TEST GET ALL
     * */

    @Test
    void testGetAllPatients() throws Exception {

        for (int i = 0; i < 10; i++) {
            PatientRequest request = new PatientRequest();
            request.setName("testbg" + i);
            request.setEmail("testbg" + i + "@gmail.com");
            request.setDateOfBirth("12-20-2003");
            request.setGender("MAN");
            request.setAddress("JALAN BARU JADI");

            patientService.createPatient(request);
        }

        mockMvc.perform(
                get("/api/patients")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<List<PatientResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });
                    int lengthPatients = response.getData().toArray().length;
                    Assertions.assertEquals(10, lengthPatients);
                    Assertions.assertEquals("OK", response.getStatus());
                }
        );
    }

    /*
     * GET PATIENTS BY ID
     * - GetPatientsByIdSuccess
     * - GetPatientsByIdFail
     * */
    @Test
    void testGetPatientsByIdSuccess() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setName("testbg");
        request.setEmail("testbg@gmail.com");
        request.setDateOfBirth("12-20-2003");
        request.setGender("MAN");
        request.setAddress("JALAN BARU JADI");

        patientService.createPatient(request);

        mockMvc.perform(
                get("/api/patients")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<List<PatientResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<List<PatientResponse>>>() {
                    });
                    int lengthPatients = response.getData().size();
                    Assertions.assertEquals(1, lengthPatients);
                    Assertions.assertEquals("OK", response.getStatus());


                    //GET BY ID
                    Long patientId = response.getData().getLast().getId();
                    mockMvc.perform(
                            get("/api/patients/" + patientId)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                    ).andExpect(
                            status().isOk()
                    ).andDo(
                            resultById -> {
                                WebResponse<PatientResponse> responseById = objectMapper.readValue(resultById.getResponse().getContentAsString(),
                                        new TypeReference<WebResponse<PatientResponse>>() {
                                });
                                Assertions.assertEquals("OK", responseById.getStatus());
                                Assertions.assertNotNull(responseById.getData());
                                Assertions.assertEquals(patientId, responseById.getData().getId());
                            }
                    );
                }
        );
    }

    @Test
    void testGetPatientsByIdFail() throws Exception {
        mockMvc.perform(
                get("/api/patients/999")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isNotFound()
        ).andDo(
                result -> {
                    ErrorResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                }
        );
    }


    /*
    * DO UPDATE
    * - testUpdatePatientSuccess
    * - testGetPatientsUpdateFailIdNotFound
    * - testGetPatientsUpdateFailCorrupt
    * */

    @Test
    void testGetPatientsUpdateSuccess() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setName("testbg");
        request.setEmail("testbg@gmail.com");
        request.setDateOfBirth("12-20-2003");
        request.setGender("MAN");
        request.setAddress("JALAN BARU JADI");

        patientService.createPatient(request);

        mockMvc.perform(
                get("/api/patients")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<List<PatientResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<List<PatientResponse>>>() {
                            });
                    int lengthPatients = response.getData().size();
                    Assertions.assertEquals(1, lengthPatients);
                    Assertions.assertEquals("OK", response.getStatus());


                    //do update
                    PatientRequest requestUpdate = new PatientRequest();
                    requestUpdate.setName("testbg");
                    requestUpdate.setEmail("update@gmail.com");
                    requestUpdate.setDateOfBirth("13-02-2006");
                    requestUpdate.setGender("girl");
                    requestUpdate.setAddress("JALAN BARU JADI");

                    Long patientId = response.getData().getLast().getId();
                    mockMvc.perform(
                            put("/api/patients/" + patientId)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(request))
                    ).andExpect(
                            status().isOk()
                    ).andDo(
                            resultById -> {
                                WebResponse<String> responseById = objectMapper.readValue(resultById.getResponse().getContentAsString(),
                                        new TypeReference<WebResponse<String>>() {
                                        });
                                Assertions.assertEquals("UPDATED", responseById.getStatus());
                                Assertions.assertNotEquals(response.getData().getLast().getEmail(), requestUpdate.getEmail());
                                Assertions.assertNotEquals(response.getData().getLast().getDateOfBirth(), requestUpdate.getDateOfBirth());
                            }
                    );
                }
        );
    }

    @Test
    void testGetPatientsUpdateFailIdNotFound() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setName("testbg");
        request.setEmail("testbg@gmail.com");
        request.setDateOfBirth("12-20-2003");
        request.setGender("MAN");
        request.setAddress("JALAN BARU JADI");

        patientService.createPatient(request);

        long wrongId = 99999L;
        mockMvc.perform(
                put("/api/patients/" + wrongId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(
                status().isNotFound()
        ).andDo(
                resultById -> {
                    ErrorResponse<String> responseById = objectMapper.readValue(resultById.getResponse().getContentAsString(),
                            new TypeReference<ErrorResponse<String>>() {
                            });
                    Assertions.assertNotNull(responseById.getErrors());
                }
        );
    }

    @Test
    void testGetPatientsUpdateFailCorrupt() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setName("testbg");
        request.setEmail("testbg@gmail.com");
        request.setDateOfBirth("12-20-2003");
        request.setGender("MAN");
        request.setAddress("JALAN BARU JADI");

        patientService.createPatient(request);

        mockMvc.perform(
                get("/api/patients")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<List<PatientResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<List<PatientResponse>>>() {
                            });
                    int lengthPatients = response.getData().size();
                    Assertions.assertEquals(1, lengthPatients);
                    Assertions.assertEquals("OK", response.getStatus());


                    //corupt no require set name
                    PatientRequest requestUpdate = new PatientRequest();
                    requestUpdate.setEmail("testbg@gmail.com");
                    requestUpdate.setDateOfBirth("13-02-2009");
                    requestUpdate.setGender("grill");
                    requestUpdate.setAddress("grill jadi baru");

                    Long patientId = response.getData().getLast().getId();
                    mockMvc.perform(
                            put("/api/patients/" + patientId)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(requestUpdate))
                    ).andExpect(
                            status().isBadRequest()
                    ).andDo(
                            resultById -> {
                                ErrorResponse<String> responseById = objectMapper.readValue(resultById.getResponse().getContentAsString(),
                                        new TypeReference<ErrorResponse<String>>() {
                                        });
                                Assertions.assertNotNull(responseById.getErrors());
                            }
                    );
                }
        );
    }

    /*
    * DO DELETE
    * - testGetPatientsDeleteSuccess
    * - testDeletePatientFail
    * */
    @Test
    void testGetPatientsDeleteSuccess() throws Exception {
        PatientRequest request = new PatientRequest();
        request.setName("testbg");
        request.setEmail("testbg@gmail.com");
        request.setDateOfBirth("12-20-2003");
        request.setGender("MAN");
        request.setAddress("JALAN BARU JADI");

        patientService.createPatient(request);

        mockMvc.perform(
                get("/api/patients")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isOk()
        ).andDo(
                result -> {
                    WebResponse<List<PatientResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<List<PatientResponse>>>() {
                            });
                    int lengthPatients = response.getData().size();
                    Assertions.assertEquals(1, lengthPatients);
                    Assertions.assertEquals("OK", response.getStatus());


                    Long patientId = response.getData().getLast().getId();
                    mockMvc.perform(
                            delete("/api/patients/" + patientId)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                    ).andExpect(
                            status().isOk()
                    ).andDo(
                            resultById -> {
                                WebResponse<String> responseById = objectMapper.readValue(resultById.getResponse().getContentAsString(),
                                        new TypeReference<WebResponse<String>>() {
                                        });
                                Assertions.assertEquals("DELETED",responseById.getStatus());
                            }
                    );
                }
        );
    }

    @Test
    void testGetPatientsDeleteFail() throws Exception {
        Long wrongId = 9999999L;
        mockMvc.perform(
                delete("/api/patients/" + wrongId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                status().isNotFound()
        ).andDo(
                resultById -> {
                    ErrorResponse<String> responseById = objectMapper.readValue(resultById.getResponse().getContentAsString(),
                            new TypeReference<ErrorResponse<String>>() {
                            });
                    Assertions.assertNotNull(responseById.getErrors());
                }
        );
    }


}
