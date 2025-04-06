package com.koriebruh.analyticservice.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);


//    @KafkaListener(topics = "${spring.kafka.topic.we-consume}", groupId = "spring.kafka.consumer.group-id")
    @KafkaListener(topics = "patient-events", groupId = "analytic-service")
    @Profile("!test")
    public void consumeEvent(byte[] event) {
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            /*HERE WE CAN DO ANYTHING WITH THE EVENT
             * Include Business Logic, for example we just log the event
             * */

            log.info("Event consumed: {}", patientEvent);

        } catch (Exception e) {
            log.error("Error deserializing event {}", e.getMessage());
        }
    }
}
