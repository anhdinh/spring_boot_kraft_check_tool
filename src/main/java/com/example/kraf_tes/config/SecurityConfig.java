package com.example.kraf_tes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        // Bỏ qua static resources
                        .pathMatchers("/css/**", "/js/**").permitAll()

                        // Bỏ qua các trang công khai
                        .pathMatchers("/", "/index.html", "/favicon.ico", "/api/latest", "/api/stream").permitAll()

                        // Bỏ qua endpoint metric Actuator / Prometheus
                        .pathMatchers("/actuator/**").permitAll()

                        // Chỉ yêu cầu auth cho các API cần bảo vệ
                        .pathMatchers("/send", "/chat-room").authenticated()

                        // Các request còn lại cần auth
                        .anyExchange().authenticated()
                )
                .formLogin(withDefaults())
                .httpBasic(withDefaults())
                // Cấu hình Logout
                .logout(logoutSpec -> logoutSpec
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(new RedirectServerLogoutSuccessHandler() {{
                            setLogoutSuccessUrl(java.net.URI.create("/?logout"));
                        }})
                )
                .build();
    }

    // Tạo User ảo trên Memory
    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("user123"))
                .roles("USER")
                .build();

        return new MapReactiveUserDetailsService(admin, user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}