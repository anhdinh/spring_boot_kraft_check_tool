# 🚀 Spring Boot Kafka KRaft - Real-time Dashboard

Dự án này là một công cụ kiểm tra (check tool) tích hợp giữa **Spring Boot WebFlux** và **Apache Kafka (chế độ KRaft)**. Hệ thống cho phép gửi message qua giao diện Web và hiển thị dữ liệu nhận được theo thời gian thực (Real-time) mà không cần tải lại trang.



## ✨ Tính năng chính
* **Kafka Producer**: Gửi message (Key/Value) tới Kafka thông qua API Reactive.
* **Kafka Consumer**: Lắng nghe dữ liệu từ Topic và xử lý không đồng bộ.
* **Real-time Stream**: Sử dụng `Sinks.Many` và `Flux` để đẩy dữ liệu qua **Server-Sent Events (SSE)**.
* **Deduplication**: Cơ chế chống trùng lặp tin nhắn tại cả Backend (Flux operators) và Frontend (JavaScript logic).
* **Modern UI**: Giao diện Bootstrap 5 chuyên nghiệp với hiệu ứng animation mượt mà.

## 🛠 Công nghệ sử dụng
* **Backend**: Java 17, Spring Boot 3.x, Spring WebFlux.
* **Messaging**: Apache Kafka (phiên bản mới nhất chạy chế độ KRaft - không cần Zookeeper).
* **Frontend**: Thymeleaf, JavaScript (EventSource API), Bootstrap 5.
* **Container**: Docker & Docker Compose.

---

## 🚀 Hướng dẫn khởi chạy nhanh

### 1. Khởi chạy Kafka Cluster
Mở terminal tại thư mục gốc của dự án và chạy:
```bash
docker-compose up -d
