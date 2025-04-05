package com.koriebruh.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Korie Bruh
 * @version 1.0
 * @since 2025-05-04
// */

@ExtendWith(SpringExtension.class)
@Import(BillingGrpcService.class)
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