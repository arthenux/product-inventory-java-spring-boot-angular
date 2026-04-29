package com.alan.product_java_spring_boot_backend.auth;

public record LoginResponse(
        String token,
        String tokenType,
        long expiresInMinutes,
        String email
) {
}
