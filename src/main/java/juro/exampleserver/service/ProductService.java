package juro.exampleserver.service;

import java.util.Collections;

import org.springframework.stereotype.Service;

import juro.exampleserver.dto.comon.PageableDto;
import juro.exampleserver.dto.product.ProductCreateRequestDto;
import juro.exampleserver.dto.product.ProductDto;
import juro.exampleserver.dto.product.ProductSearchRequestDto;
import juro.exampleserver.exception.ClientException;
import juro.exampleserver.exception.ErrorCode;
import juro.exampleserver.repository.product.Product;
import juro.exampleserver.repository.product.ProductRepository;
import juro.exampleserver.repository.product.ProductStatus;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public ProductDto createProduct(ProductCreateRequestDto requestDto) {
		Product product = Product.builder()
			.userId(requestDto.getUserId())
			.name(requestDto.getName())
			.price(requestDto.getPrice())
			.quantity(requestDto.getQuantity())
			.status(ProductStatus.AVAILABLE)
			.build();

		Product savedProduct = productRepository.save(product);

		return ProductDto.of(savedProduct);

	}

	public ProductDto getProduct(Long id) {
		Product product = productRepository.findById(id)
			.orElseThrow(
				() -> new ClientException(ErrorCode.BAD_REQUEST, "there is not product. productId = %s".formatted(id)));

		return ProductDto.of(product);
	}

	public PageableDto<ProductDto> searchProducts(ProductSearchRequestDto requestDto) {

		return PageableDto.<ProductDto>builder()
			.items(Collections.emptyList())
			.next(null)
			.totalCount(0L)
			.build();
	}
}
