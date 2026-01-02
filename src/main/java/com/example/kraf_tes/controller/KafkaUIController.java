package com.example.kraf_tes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Controller
public class KafkaUIController {
    @GetMapping("/")
    public Mono<String> index(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("username", principal.getName());
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        return Mono.just("index-form");
    }

    @GetMapping("/chat-room")
    public Mono<String> showForm() {
        return Mono.just("kafka-form");
    }
}
