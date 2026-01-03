# 🚀 Spring Boot Kafka KRaft - Real-time Dashboard

Dự án này là một **công cụ kiểm tra tích hợp** giữa **Spring Boot WebFlux** và **Apache Kafka (KRaft mode)**.  
Hệ thống cho phép gửi message qua giao diện Web và hiển thị dữ liệu **real-time** mà không cần tải lại trang.

---

## ✨ Tính năng chính

- **Kafka Producer**: Gửi message (Key/Value) tới Kafka qua API Reactive
- **Kafka Consumer**: Lắng nghe dữ liệu từ Topic và xử lý không đồng bộ
- **Real-time Stream**: Sử dụng `Sinks.Many` + `Flux` với **Server-Sent Events (SSE)**
- **Deduplication**: Chống trùng lặp tin nhắn Backend + Frontend
- **Metrics & Monitoring**: Spring Actuator + Prometheus
- **Modern UI**: Bootstrap 5 + JavaScript EventSource API

---

## 🛠 Công nghệ sử dụng

- **Backend**: Java 17, Spring Boot 3.x, WebFlux
- **Messaging**: Apache Kafka (KRaft mode)
- **Frontend**: Thymeleaf, JavaScript, Bootstrap 5
- **Monitoring**: Prometheus
- **Container**: Docker & Docker Compose

---

## 🚀 Hướng dẫn khởi chạy nhanh

### 1️⃣ Khởi chạy Kafka Cluster

```bash
.\gradlew clean build -x test;docker build -t myapp:v1.0 .;docker-compose up -d
