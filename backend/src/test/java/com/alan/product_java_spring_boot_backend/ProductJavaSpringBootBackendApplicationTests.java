package com.alan.product_java_spring_boot_backend;

import com.alan.product_java_spring_boot_backend.auth.AuthService;
import com.alan.product_java_spring_boot_backend.auth.JwtService;
import com.alan.product_java_spring_boot_backend.auth.LoginRequest;
import com.alan.product_java_spring_boot_backend.auth.LoginResponse;
import com.alan.product_java_spring_boot_backend.product.ProductRepository;
import com.alan.product_java_spring_boot_backend.user.AppUser;
import com.alan.product_java_spring_boot_backend.user.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductJavaSpringBootBackendApplicationTests {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthService authService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void loadsSampleProducts() {
		Assertions.assertEquals(10, productRepository.count());
	}

	@Test
	void loadsSeededUserWithHashedPassword() {
		AppUser user = userRepository.findByEmail("test-user@example.com").orElseThrow();

		Assertions.assertTrue(passwordEncoder.matches("test-password", user.getPassword()));
		Assertions.assertNotEquals("test-password", user.getPassword());
	}

	@Test
	void logsInSeededUserAndIssuesJwt() {
		LoginResponse response = authService.login(new LoginRequest("test-user@example.com", "test-password"));

		Assertions.assertEquals("Bearer", response.tokenType());
		Assertions.assertEquals("test-user@example.com", response.email());
		Assertions.assertEquals("test-user@example.com", jwtService.extractUsername(response.token()));
	}

	@Test
	void exposesLoginEndpoint() throws Exception {
		mockMvc.perform(post("/api/auth/login")
						.contextPath("/api")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "email": "test-user@example.com",
								  "password": "test-password"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.tokenType").value("Bearer"))
				.andExpect(jsonPath("$.email").value("test-user@example.com"));
	}

	@Test
	void rejectsProductRequestsWithoutJwt() throws Exception {
		mockMvc.perform(get("/api/products").contextPath("/api"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	void listsProductsWithJwt() throws Exception {
		mockMvc.perform(get("/api/products")
						.contextPath("/api")
						.header(HttpHeaders.AUTHORIZATION, authorizationHeader()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()").value(10));
	}

	@Test
	void createsUpdatesAndDeletesProductWithJwt() throws Exception {
		MvcResult createResult = mockMvc.perform(post("/api/products")
						.contextPath("/api")
						.header(HttpHeaders.AUTHORIZATION, authorizationHeader())
						.contentType(MediaType.APPLICATION_JSON)
						.content(productJson("Logitech Brio 4K Webcam", "Logitech", "CAM-LOG-BRIO4K", "SMART_HOME")))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Logitech Brio 4K Webcam"))
				.andExpect(jsonPath("$.sku").value("CAM-LOG-BRIO4K"))
				.andReturn();

		Integer productId = JsonPath.read(createResult.getResponse().getContentAsString(), "$.id");

		mockMvc.perform(put("/api/products/{id}", productId)
						.contextPath("/api")
						.header(HttpHeaders.AUTHORIZATION, authorizationHeader())
						.contentType(MediaType.APPLICATION_JSON)
						.content(productJson("Logitech Brio 4K Ultra HD Webcam", "Logitech", "CAM-LOG-BRIO4K", "SMART_HOME")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Logitech Brio 4K Ultra HD Webcam"));

		mockMvc.perform(delete("/api/products/{id}", productId)
						.contextPath("/api")
						.header(HttpHeaders.AUTHORIZATION, authorizationHeader()))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/api/products/{id}", productId)
						.contextPath("/api")
						.header(HttpHeaders.AUTHORIZATION, authorizationHeader()))
				.andExpect(status().isNotFound());
	}

	@Test
	void rejectsDuplicateSku() throws Exception {
		mockMvc.perform(post("/api/products")
						.contextPath("/api")
						.header(HttpHeaders.AUTHORIZATION, authorizationHeader())
						.contentType(MediaType.APPLICATION_JSON)
						.content(productJson("Duplicate TV", "Samsung", "TV-SAM-OLED55", "TV_AND_HOME_CINEMA")))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.message").value("Product SKU already exists: TV-SAM-OLED55"));
	}

	private String authorizationHeader() {
		LoginResponse response = authService.login(new LoginRequest("test-user@example.com", "test-password"));
		return "Bearer " + response.token();
	}

	private String productJson(String name, String brand, String sku, String category) {
		return """
				{
				  "name": "%s",
				  "brand": "%s",
				  "sku": "%s",
				  "category": "%s",
				  "price": 149.99,
				  "stockQuantity": 7,
				  "description": "A product created by the secured API test."
				}
				""".formatted(name, brand, sku, category);
	}

}
