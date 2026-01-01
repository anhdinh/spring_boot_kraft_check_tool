package com.example.kraf_tes.controller;
import com.example.kraf_tes.service.KafkaStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;


@RestController
@RequiredArgsConstructor
public class KafkaWebController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaStreamService streamService;

    private static final String TOPIC = "chat-room";



    @PostMapping("/send")
    public Mono<Map<String, String>> sendMessage(@RequestBody Mono<Map<String, String>> body) {
        return body.flatMap(data -> {
            String key = data.get("key");
            String message = data.get("message");

            // kafkaTemplate.send trả về CompletableFuture trong Spring Kafka 3.x
            return Mono.fromFuture(kafkaTemplate.send(TOPIC, key, message))
                    .map(result -> Map.of(
                            "status", "success",
                            "offset", String.valueOf(result.getRecordMetadata().offset()),
                            "message", "Đã gửi: " + message
                    ))
                    .onErrorResume(ex -> Mono.just(Map.of(
                            "status", "failed",
                            "error", ex.getMessage()
                    )));
        });
    }

    @GetMapping(value = "/api/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamMessages() {
        return Flux.merge(
                        streamService.getMessageStream(),
                        // Gửi một chuỗi rỗng mỗi 15 giây để giữ kết nối không bị timeout
                        Flux.interval(Duration.ofSeconds(15)).map(i -> "heartbeat:ping")
                )
                .doOnSubscribe(s -> System.out.println(">>> New subscriber connected"))
                .doOnCancel(() -> System.out.println("<<< Subscriber disconnected"));
    }

}