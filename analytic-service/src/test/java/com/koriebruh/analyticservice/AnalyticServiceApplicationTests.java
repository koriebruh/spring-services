package com.koriebruh.analyticservice;

import com.koriebruh.analyticservice.kafka.KafkaConsumer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.doNothing;

@SpringBootTest
@ActiveProfiles("test")
class AnalyticServiceApplicationTests {

    @MockBean
    private KafkaConsumer kafkaConsumer;

    @Test
    void contextLoads() {
        doNothing().when(kafkaConsumer).consumeEvent(Mockito.any(byte[].class));

    }

}
