package juro.exampleserver.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import juro.exampleserver.controller.model.product.ProductCreateRequest;
import juro.exampleserver.controller.model.product.ProductResponse;
import juro.exampleserver.dto.common.ApiResponse;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerIntegrationTest {

	@LocalServerPort
	private int port;

	private String getBaseUrl() {
		return "http://localhost:" + port;
	}

	@Test
	public void testGetProduct() {
		// when
		RestClient restClient = RestClient.create(getBaseUrl());

		ApiResponse<ProductResponse> response = restClient.get()
			.uri(getBaseUrl() + "/v1/products/{id}", 1L)
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});

		// then
		assertThat(response).isNotNull();
		assertThat(response.getErrorCode()).isNull();
		assertThat(response.getBody().getId()).isEqualTo(1);
	}

	@Test
	public void testCreateProduct() {
		// given
		ProductCreateRequest request = ProductCreateRequest.builder()
			.name("Test Product")
			.price(1000L)
			.quantity(10L)
			.build();

		// when
		RestClient restClient = RestClient.create(getBaseUrl());
		ApiResponse<ProductResponse> response = restClient.post()
			.uri(getBaseUrl() + "/v1/products")
			.contentType(MediaType.APPLICATION_JSON)
			.body(request)
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});

		assertThat(response).isNotNull();
		assertThat(response.getErrorCode()).isNull();
		assertThat(response.getBody().getName()).isEqualTo("Test Product");
	}
}
