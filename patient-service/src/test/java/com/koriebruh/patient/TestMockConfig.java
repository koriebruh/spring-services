package com.koriebruh.patient;

import com.koriebruh.patient.grpc.BillingServiceGrpcClient;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestMockConfig {

    @Bean
    public BillingServiceGrpcClient billingServiceGrpcClient() {
        return Mockito.mock(BillingServiceGrpcClient.class);
    }
}
