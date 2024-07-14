package juro.exampleserver.controller;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;

import juro.exampleserver.config.DebuggingAuth;
import juro.exampleserver.controller.model.common.ApiResponse;
import juro.exampleserver.controller.model.common.PageResponse;
import juro.exampleserver.controller.model.product.ProductCreateRequest;
import juro.exampleserver.controller.model.product.ProductResponse;
import juro.exampleserver.repository.product.Product;
import juro.exampleserver.repository.product.ProductRepository;
import juro.exampleserver.repository.product.ProductStatus;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerIntegrationTest {

	@LocalServerPort
	private int port;
	@Autowired
	private DebuggingAuth debuggingAuth;
	@Autowired
	private ProductRepository productRepository;

	private String getBaseUrl() {
		return "http://localhost:" + port;
	}

	// @BeforeEach
	// public void beforeEach() {
	// 	productRepository.deleteAll();
	// }

	@Test
	@DisplayName("getProduct - product 조회 성공")
	public void testGetProduct() {
		//given
		Product product = Product.builder()
			.userId(1L)
			.name("testProduct")
			.price(1000L)
			.quantity(10L)
			.status(ProductStatus.AVAILABLE)
			.build();
		Product savedProduct = productRepository.save(product);

		// when
		RestClient restClient = RestClient.create(getBaseUrl());

		ApiResponse<ProductResponse> response = restClient.get()
			.uri(getBaseUrl() + "/v1/products/{id}", savedProduct.getId())
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});

		// then
		assertThat(response).isNotNull();
		assertThat(response.getErrorCode()).isNull();
		assertThat(response.getBody().getId()).isEqualTo(savedProduct.getId());
	}

	@Test
	@DisplayName("createProduct - product 생성 성공")
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
			.header(HttpHeaders.AUTHORIZATION, debuggingAuth.getBearer())
			.body(request)
			.retrieve()
			.body(new ParameterizedTypeReference<>() {
			});

		assertThat(response).isNotNull();
		assertThat(response.getErrorCode()).isNull();
		assertThat(response.getBody().getName()).isEqualTo("Test Product");
	}
	//
	// @Test
	// public void testSearchProducts() {
	// 	// given
	// 	Product product1 = Product.builder()
	// 		.userId(1L)
	// 		.name("testProduct")
	// 		.price(1000L)
	// 		.quantity(10L)
	// 		.status(ProductStatus.AVAILABLE)
	// 		.build();
	//
	// 	Product product2 = Product.builder()
	// 		.userId(1L)
	// 		.name("testProduct")
	// 		.price(1000L)
	// 		.quantity(10L)
	// 		.status(ProductStatus.AVAILABLE)
	// 		.build();
	// 	Product product3 = Product.builder()
	// 		.userId(1L)
	// 		.name("testProduct")
	// 		.price(1000L)
	// 		.quantity(10L)
	// 		.status(ProductStatus.AVAILABLE)
	// 		.build();
	//
	// 	Product product4 = Product.builder()
	// 		.userId(2L)
	// 		.name("testProduct")
	// 		.price(1000L)
	// 		.quantity(10L)
	// 		.status(ProductStatus.AVAILABLE)
	// 		.build();
	//
	// 	productRepository.saveAll(List.of(product1, product2, product3, product4));
	//
	// 	String url = "/v1/products?size=2&productStatus=AVAILABLE&userId=1";
	// 	RestClient restClient = RestClient.create(getBaseUrl());
	//
	// 	// when
	// 	ApiResponse<PageResponse<ProductResponse>> response =
	// 		restClient.get().uri(url).retrieve().body(
	// 			new ParameterizedTypeReference<>() {
	// 			});
	//
	// 	// then
	// 	assertThat(response).isNotNull();
	// 	PageResponse<ProductResponse> body = response.getBody();
	// 	assertThat(body.getTotalCount()).isEqualTo(3);
	// 	assertThat(body.getItems()).size().isEqualTo(2);
	// 	assertThat(body.getNext()).isNotNull();
	// }
}
