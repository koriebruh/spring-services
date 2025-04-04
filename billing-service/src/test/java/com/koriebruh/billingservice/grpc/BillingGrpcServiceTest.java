package com.koriebruh.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BillingGrpcServiceTest {

    private BillingGrpcService billingGrpcService;

    private StreamObserver<BillingResponse> responseObserver;

    @BeforeEach
    void setUp() {
        billingGrpcService = new BillingGrpcService();
        responseObserver = mock(StreamObserver.class);
    }

    @Test
    void testCreateBillingAccount() {

        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId(1)
                .build();

        billingGrpcService.createBillingAccount(request, responseObserver);

        // Verify that the response observer was called with the expected response
        verify(responseObserver, times(1)).onNext(any(BillingResponse.class));
        verify(responseObserver, times(1)).onCompleted();

        // Assert
        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId(1)
                .setStatus("ACTIVE")
                .build();

        // Check the response content
        verify(responseObserver).onNext(response);
    }
}