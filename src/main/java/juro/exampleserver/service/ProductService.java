package juro.exampleserver.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import juro.exampleserver.dto.comon.PageableDto;
import juro.exampleserver.dto.product.ProductCreateRequestDto;
import juro.exampleserver.dto.product.ProductDto;
import juro.exampleserver.dto.product.ProductSearchRequestDto;
import juro.exampleserver.exception.ClientException;
import juro.exampleserver.exception.ErrorCode;
import juro.exampleserver.repository.common.PageResult;
import juro.exampleserver.repository.product.Product;
import juro.exampleserver.repository.product.ProductRepository;
import juro.exampleserver.repository.product.ProductSearchCriteria;
import juro.exampleserver.repository.product.ProductStatus;
import juro.exampleserver.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final UserRepository userRepository;

	@Transactional
	public ProductDto createProduct(ProductCreateRequestDto requestDto) {
		if (!userRepository.existsById(requestDto.getUserId())) {
			throw new ClientException(ErrorCode.BAD_REQUEST,
				"cannot create product because user not found. userId=%s".formatted(requestDto.getUserId()));
		}

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

	@Transactional(readOnly = true)
	public ProductDto getProduct(Long id) {
		Product product = productRepository.findById(id)
			.orElseThrow(
				() -> new ClientException(ErrorCode.BAD_REQUEST, "there is not product. productId = %s".formatted(id)));

		return ProductDto.of(product);
	}

	@Transactional(readOnly = true)
	public PageableDto<ProductDto> searchProducts(ProductSearchRequestDto requestDto) {
		ProductSearchCriteria criteria = requestDto.toCriteria();

		PageResult<Product> search = productRepository.search(criteria);

		return PageableDto.<ProductDto>builder()
			.items(search.getItems().stream().map(ProductDto::of).toList())
			.nextSearchAfter(search.getNextSearchAfter())
			.totalCount(search.getTotalCount())
			.build();
	}
}
