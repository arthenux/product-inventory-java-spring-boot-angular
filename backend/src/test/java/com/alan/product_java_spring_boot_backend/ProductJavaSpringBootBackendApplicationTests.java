package com.alan.product_java_spring_boot_backend;

import com.alan.product_java_spring_boot_backend.auth.AuthService;
import com.alan.product_java_spring_boot_backend.auth.JwtService;
import com.alan.product_java_spring_boot_backend.auth.LoginRequest;
import com.alan.product_java_spring_boot_backend.auth.LoginResponse;
import com.alan.product_java_spring_boot_backend.product.ProductRepository;
import com.alan.product_java_spring_boot_backend.user.AppUser;
import com.alan.product_java_spring_boot_backend.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

}
