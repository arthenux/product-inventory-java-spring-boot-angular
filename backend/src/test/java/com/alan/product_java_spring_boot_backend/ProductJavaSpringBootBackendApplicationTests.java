package com.alan.product_java_spring_boot_backend;

import com.alan.product_java_spring_boot_backend.product.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductJavaSpringBootBackendApplicationTests {

	@Autowired
	private ProductRepository productRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void loadsSampleProducts() {
		Assertions.assertEquals(10, productRepository.count());
	}

}
