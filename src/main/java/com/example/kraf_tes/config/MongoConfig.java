package com.example.kraf_tes.config;

import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Configuration
@Log4j2
public class MongoConfig {

    @Bean
    CommandLineRunner initMongoRequirements(ReactiveMongoTemplate reactiveMongoTemplate) {
        return args -> {
            String collectionName = "messages";

            reactiveMongoTemplate.collectionExists(collectionName)
                    .flatMap(exists -> {
                        if (!exists) {
                            return reactiveMongoTemplate.createCollection(collectionName).thenReturn(true);
                        }
                        return Mono.just(true);
                    })
                    .then(
                            // Sử dụng phương thức không bị deprecated
                            reactiveMongoTemplate.getCollection(collectionName)
                                    .flatMap(collection -> {
                                        // Tạo Index cho trường created_time tăng dần
                                        // TTL: 7 ngày (604800 giây)
                                        IndexOptions options = new IndexOptions()
                                                .name("ttl_created_time_index")
                                                .expireAfter(7L, TimeUnit.DAYS);

                                        // Sử dụng Mono.from để chuyển đổi từ Publisher của Driver sang Reactor Mono
                                        return Mono.from(collection.createIndex(Indexes.ascending("created_time"), options));
                                    })
                    )
                    .subscribe(
                            indexName -> log.info("--- Khởi tạo MongoDB & TTL Index thành công: {} ---", indexName),
                            error -> {
                                // Xử lý lỗi trùng tên index như bạn gặp lúc nãy
                                if (error.getMessage().contains("IndexOptionsConflict")) {
                                    log.warn("--- Index đã tồn tại với tên hoặc cấu hình khác. Hãy xóa cũ nếu muốn đổi mới ---");
                                } else {
                                    log.error("--- Lỗi khởi tạo MongoDB: {} ---", error.getMessage());
                                }
                            }
                    );
        };
    }
}