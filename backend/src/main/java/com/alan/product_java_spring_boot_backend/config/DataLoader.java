package com.alan.product_java_spring_boot_backend.config;

import com.alan.product_java_spring_boot_backend.product.Product;
import com.alan.product_java_spring_boot_backend.product.ProductCategory;
import com.alan.product_java_spring_boot_backend.product.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            return;
        }

        productRepository.saveAll(sampleProducts());
    }

    private List<Product> sampleProducts() {
        return List.of(
                new Product(
                        "Samsung 55-inch 4K OLED Smart TV",
                        "Samsung",
                        "TV-SAM-OLED55",
                        ProductCategory.TV_AND_HOME_CINEMA,
                        new BigDecimal("1299.00"),
                        8,
                        "Premium OLED smart TV with cinematic contrast, 4K upscaling, and built-in streaming apps."
                ),
                new Product(
                        "LG 65-inch QNED 4K Smart TV",
                        "LG",
                        "TV-LG-QNED65",
                        ProductCategory.TV_AND_HOME_CINEMA,
                        new BigDecimal("999.00"),
                        6,
                        "Large-screen QNED television with vivid colour, 120 Hz motion, and voice assistant support."
                ),
                new Product(
                        "Apple MacBook Air 13-inch M3",
                        "Apple",
                        "LAP-APL-MBA13M3",
                        ProductCategory.LAPTOPS,
                        new BigDecimal("1099.00"),
                        10,
                        "Lightweight laptop with Apple silicon performance, all-day battery life, and a Liquid Retina display."
                ),
                new Product(
                        "HP Pavilion 15 Laptop",
                        "HP",
                        "LAP-HP-PAV15",
                        ProductCategory.LAPTOPS,
                        new BigDecimal("649.99"),
                        14,
                        "Everyday Windows laptop with a 15-inch display, fast SSD storage, and a comfortable keyboard."
                ),
                new Product(
                        "Dell 27-inch QHD USB-C Monitor",
                        "Dell",
                        "MON-DEL-QHD27",
                        ProductCategory.MONITORS,
                        new BigDecimal("329.99"),
                        16,
                        "Sharp QHD monitor with USB-C connectivity, slim bezels, and an ergonomic adjustable stand."
                ),
                new Product(
                        "Samsung Galaxy S24 128 GB",
                        "Samsung",
                        "PHN-SAM-S24128",
                        ProductCategory.MOBILE_PHONES,
                        new BigDecimal("799.00"),
                        12,
                        "Android smartphone with a bright AMOLED display, advanced camera system, and 5G connectivity."
                ),
                new Product(
                        "Logitech MX Keys S Wireless Keyboard",
                        "Logitech",
                        "KEY-LOG-MXKEYSS",
                        ProductCategory.KEYBOARDS,
                        new BigDecimal("109.99"),
                        20,
                        "Low-profile wireless keyboard with smart backlighting, quiet typing, and multi-device pairing."
                ),
                new Product(
                        "Logitech MX Master 3S Wireless Mouse",
                        "Logitech",
                        "MSE-LOG-MXM3S",
                        ProductCategory.MICE,
                        new BigDecimal("99.99"),
                        18,
                        "Ergonomic wireless mouse with precise tracking, quiet clicks, and fast electromagnetic scrolling."
                ),
                new Product(
                        "Sony WH-1000XM5 Wireless Headphones",
                        "Sony",
                        "AUD-SON-WHXM5",
                        ProductCategory.HEADPHONES,
                        new BigDecimal("299.00"),
                        9,
                        "Noise-cancelling over-ear headphones with premium sound, long battery life, and multipoint Bluetooth."
                ),
                new Product(
                        "Samsung T7 Shield 1 TB Portable SSD",
                        "Samsung",
                        "STR-SAM-T7S1TB",
                        ProductCategory.STORAGE,
                        new BigDecimal("119.99"),
                        22,
                        "Rugged portable SSD with fast USB-C transfers, compact design, and hardware encryption support."
                )
        );
    }
}
