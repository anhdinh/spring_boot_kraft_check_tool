package com.example.kraf_tes.consumer;

import com.example.kraf_tes.service.KafkaStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final KafkaStreamService streamService;

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "mytopic", groupId = "my-group")
    public void consume(String message) {
        streamService.pushMessage(message);streamService.pushMessage(message);
    }
}