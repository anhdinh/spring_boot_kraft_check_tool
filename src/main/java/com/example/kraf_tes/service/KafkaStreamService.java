package com.example.kraf_tes.service;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class KafkaStreamService {

    private final Sinks.Many<String> sink = Sinks.many().multicast().directBestEffort();

    // Gửi message tới tất cả subscriber hiện tại
    public void pushMessage(String message) {
        sink.tryEmitNext(message)
                .orThrow(); // hoặc ignore lỗi nếu muốn
    }

    // Flux cho các subscriber mới
    public Flux<String> getMessageStream() {
        return sink.asFlux();
    }
}

