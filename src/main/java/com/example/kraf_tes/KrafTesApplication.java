package com.example.kraf_tes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class KrafTesApplication {

	public static void main(String[] args) {
		SpringApplication.run(KrafTesApplication.class, args);
	}

}
