package juro.exampleserver.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import juro.exampleserver.controller.model.product.ProductCreateRequest;
import juro.exampleserver.controller.model.product.ProductResponse;
import juro.exampleserver.dto.common.ApiResponse;
import juro.exampleserver.repository.product.ProductStatus;

@Validated
@RestController
public class ProductController {

	@GetMapping("/v1/products/{id}")
	public ApiResponse<ProductResponse> getProduct(Long id) {
		ProductResponse productResponse = ProductResponse.builder()
			.id(1L)
			.name("Test Product")
			.price(1000L)
			.quantity(10L)
			.status(ProductStatus.AVAILABLE)
			.build();

		return ApiResponse.success(productResponse);
	}

	@PostMapping("/v1/products")
	public ApiResponse<ProductResponse> createProduct(@RequestBody @Valid ProductCreateRequest request) {
		ProductResponse productResponse = ProductResponse.builder()
			.id(1L)
			.name(request.getName())
			.price(request.getPrice())
			.quantity(request.getQuantity())
			.status(ProductStatus.AVAILABLE)
			.build();

		return ApiResponse.success(productResponse);
	}

}
