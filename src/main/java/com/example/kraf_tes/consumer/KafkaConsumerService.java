package com.example.kraf_tes.consumer;

import com.example.kraf_tes.service.KafkaStreamService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaStreamService streamService;

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "chat-room", groupId = "my-group-chat-room")
    public void consume(ConsumerRecord<String, String> record) throws Exception {
        String key = record.key();
        String value = record.value();

        Map<String, String> payload = new HashMap<>();
        payload.put("user", key);
        payload.put("message", value);

        String jsonString = objectMapper.writeValueAsString(payload);

        streamService.pushMessage(jsonString); // gửi JSON string
    }
}