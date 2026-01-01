package com.example.kraf_tes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller
public class KafkaUIController {
    @GetMapping("/")
    public Mono<String> showForm() {
        return Mono.just("kafka-form");
    }
}
