package com.koriebruh.patient;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

@Disabled("Context loading test disabled to avoid gRPC connection issue")
@SpringBootTest
class PatientServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
