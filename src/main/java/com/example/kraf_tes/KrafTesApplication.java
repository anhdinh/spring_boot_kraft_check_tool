package com.example.kraf_tes;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
@Slf4j
public class KrafTesApplication {

	public static void main(String[] args) {
		SpringApplication.run(KrafTesApplication.class, args);
	}

	@Bean
	public CommandLineRunner printEnv(Environment env) {
		return args -> {
			System.out.println("======= TẤT CẢ BIẾN MÔI TRƯỜNG =======");

			// 1. In từ System Environment (Biến của OS/Docker/K8S)
			System.getenv().forEach((key, value) -> {
				System.out.println(key + " = " + value);
			});

			System.out.println("======= CẤU HÌNH SPRING ĐANG NHẬN =======");

			// 2. Kiểm tra riêng các biến quan trọng của Kafka
			System.out.println("KAFKA_SERVERS: " + env.getProperty("spring.kafka.bootstrap-servers"));
			System.out.println("IMAGE_BUILD: " + env.getProperty("IMAGE_BUILD"));

			System.out.println("=======================================");
		};
	}
}