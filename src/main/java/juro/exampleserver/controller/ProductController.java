package juro.exampleserver.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import juro.exampleserver.config.LoginUser;
import juro.exampleserver.config.model.ServiceUser;
import juro.exampleserver.controller.model.product.ProductCreateRequest;
import juro.exampleserver.controller.model.product.ProductResponse;
import juro.exampleserver.dto.common.ApiResponse;
import juro.exampleserver.dto.product.ProductCreateRequestDto;
import juro.exampleserver.dto.product.ProductDto;
import juro.exampleserver.service.ProductService;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;

	@GetMapping("/v1/products/{id}")
	public ApiResponse<ProductResponse> getProduct(@PathVariable Long id) {
		ProductDto productDto = productService.getProduct(id);

		ProductResponse response = ProductResponse.of(productDto);
		return ApiResponse.success(response);
	}

	@LoginUser
	@PostMapping("/v1/products")
	public ApiResponse<ProductResponse> createProduct(
		@AuthenticationPrincipal ServiceUser user,
		@RequestBody @Valid ProductCreateRequest request
	) {

		ProductCreateRequestDto requestDto = request.toDto(user.getId());
		ProductDto product = productService.createProduct(requestDto);

		ProductResponse productResponse = ProductResponse.of(product);
		return ApiResponse.success(productResponse);
	}

}
