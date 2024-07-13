package juro.exampleserver.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import juro.exampleserver.controller.model.product.ProductCreateRequest;
import juro.exampleserver.controller.model.product.ProductResponse;
import juro.exampleserver.dto.common.ApiResponse;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String getBaseUrl() {
		return "http://localhost:" + port + "/v1/products";
	}

	@Test
	public void testGetProduct() {
		// Assume there's a product with id 1 for this test
		ResponseEntity<ApiResponse> response = restTemplate.getForEntity(getBaseUrl() + "/1", ApiResponse.class);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		ApiResponse<ProductResponse> apiResponse = response.getBody();
		assertThat(apiResponse).isNotNull();
		assertThat(apiResponse.getBody().getId()).isEqualTo(1);
	}

	@Test
	public void testCreateProduct() {
		ProductCreateRequest request = new ProductCreateRequest();
		request.setName("Test Product");
		request.setPrice(1000L);
		request.setQuantity(10L);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");

		HttpEntity<ProductCreateRequest> entity = new HttpEntity<>(request, headers);
		ResponseEntity<ApiResponse> response = restTemplate.postForEntity(getBaseUrl(), entity, ApiResponse.class);

		assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
		ApiResponse<ProductResponse> apiResponse = response.getBody();
		assertThat(apiResponse).isNotNull();
		assertThat(apiResponse.getBody().getName()).isEqualTo("Test Product");
	}
}
