package com.koriebruh.patient.kafka;

import com.koriebruh.patient.entity.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
public class KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Value("${spring.kafka.topic.patient-domain}")
    private String topic;


    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendEvent(Patient patient, String eventType) {
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType(eventType)
                .build();
        try {
            log.info("Sending event to Kafka: {}", event);
            kafkaTemplate.send(topic, event.toByteArray());
        } catch (Exception e) {
            // should be handled in a better way, with trow error or retry,
            // but for now just log the error, next time will be updated
            log.error("Error sending event to Kafka", e);
        }
    }

}
