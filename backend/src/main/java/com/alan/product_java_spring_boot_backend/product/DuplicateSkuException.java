package com.alan.product_java_spring_boot_backend.product;

public class DuplicateSkuException extends RuntimeException {

    public DuplicateSkuException(String sku) {
        super("Product SKU already exists: " + sku);
    }
}
