package com.example.kraf_tes.controller;
import com.example.kraf_tes.service.KafkaStreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;

@RestController
@RequiredArgsConstructor
@Log4j2
public class KafkaWebController {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaStreamService streamService;
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private static final String TOPIC = "chat-room";


    @PostMapping("/send")
    public Mono<Map<String, String>> sendMessage(@RequestBody Mono<Map<String, String>> body) {
        return body.flatMap(data -> {
            String key = data.get("key");
            String user = data.getOrDefault("user", key);
            String message = data.get("message");

            // 1. Chuẩn bị dữ liệu để lưu vào MongoDB
            Map<String, Object> mongoData = new HashMap<>();
            mongoData.put("user", user);
            mongoData.put("message", message);
            mongoData.put("created_time", LocalDateTime.now());

            // 2. Thực hiện luồng: Save Mongo -> Send Kafka
            return reactiveMongoTemplate.save(mongoData, "messages")
                    .flatMap(savedDoc -> {
                        // Sau khi save Mongo thành công, mới gửi Kafka
                        return Mono.fromFuture(kafkaTemplate.send(TOPIC, key, message));
                    })
                    .map(result -> Map.of(
                            "status", "success",
                            "db_status", "Saved to MongoDB",
                            "offset", String.valueOf(result.getRecordMetadata().offset()),
                            "message", "Đã gửi Kafka: " + message
                    ))
                    .onErrorResume(ex -> {
                        // Xử lý lỗi nếu 1 trong 2 bước thất bại
                        log.error("Lỗi quá trình xử lý: {}", ex.getMessage());
                        return Mono.just(Map.of(
                                "status", "failed",
                                "error", "Process failed: " + ex.getMessage()
                        ));
                    });
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

    @GetMapping("/api/latest")
    public Flux<Map> getLatestMessages() {
        // 1. Tạo Query
        Query query = new Query();

        // 2. Sắp xếp theo created_time giảm dần (Mới nhất lên đầu)
        query.with(Sort.by(Sort.Direction.ASC, "created_time"));

        // 3. Giới hạn 10 bản ghi
        query.limit(10);

        // 4. Thực thi và trả về Flux (dòng dữ liệu bất đồng bộ)
        return reactiveMongoTemplate.find(query, Map.class, "messages");
    }

}