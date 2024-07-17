package juro.exampleserver.controller.product;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import juro.exampleserver.config.auth.LoginUser;
import juro.exampleserver.config.auth.ServiceUser;
import juro.exampleserver.controller.common.ApiResponse;
import juro.exampleserver.controller.common.PageResponse;
import juro.exampleserver.controller.product.model.ProductCreateRequest;
import juro.exampleserver.controller.product.model.ProductResponse;
import juro.exampleserver.service.common.PageableDto;
import juro.exampleserver.service.product.model.ProductCreateRequestDto;
import juro.exampleserver.service.product.model.ProductDto;
import juro.exampleserver.service.product.model.ProductSearchRequestDto;
import juro.exampleserver.repository.product.ProductSortType;
import juro.exampleserver.repository.product.ProductStatus;
import juro.exampleserver.service.product.ProductService;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;

	@GetMapping("/v1/products")
	public ApiResponse<PageResponse<ProductResponse>> searchProducts(
		@RequestParam @Min(1) Integer size,
		@RequestParam(required = false) String searchAfter,
		@RequestParam(required = false) ProductStatus productStatus,
		@RequestParam(required = false) Long userId,
		@RequestParam ProductSortType sort,
		@RequestParam(required = false, defaultValue = "false") boolean withTotalCount
	) {
		ProductSearchRequestDto requestDto = ProductSearchRequestDto.builder()
			.productStatus(productStatus)
			.userId(userId)
			.size(size)
			.searchAfter(searchAfter)
			.sort(sort)
			.withTotalCount(withTotalCount)
			.build();

		PageableDto<ProductDto> dto = productService.searchProducts(requestDto);
		PageResponse<ProductResponse> response = PageResponse.<ProductResponse>builder()
			.items(dto.getItems().stream().map(ProductResponse::of).toList())
			.totalCount(dto.getTotalCount())
			.nextSearchAfter(dto.getNextSearchAfter())
			.build();

		return ApiResponse.success(response);
	}

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