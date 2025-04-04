package com.koriebruh.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {

    public Logger log = LoggerFactory.getLogger(BillingGrpcService.class);


    @Override
    public void createBillingAccount(BillingRequest request, StreamObserver<BillingResponse> responseObserver) {
        log.info("Received request to create billing account for patient ID: {}", request.getPatientId());

        BillingResponse.Builder responseBuilder = BillingResponse.newBuilder()
                .setAccountId(request.getPatientId())
                .setStatus("ACTIVE");

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
