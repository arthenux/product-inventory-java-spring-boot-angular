package com.alan.product_java_spring_boot_backend.product;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Long id,
        String name,
        String brand,
        String sku,
        ProductCategory category,
        BigDecimal price,
        int stockQuantity,
        String description,
        Instant createdAt,
        Instant updatedAt
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getSku(),
                product.getCategory(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getDescription(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
