package juro.exampleserver.service.product;

import org.springframework.stereotype.Component;

import juro.exampleserver.exception.ClientException;
import juro.exampleserver.exception.ErrorCode;
import juro.exampleserver.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductValidator {
	private final ProductRepository productRepository;

	public void validateProductExists(Long productId) {
		if (!productRepository.existsById(productId)) {
			throw new ClientException(ErrorCode.BAD_REQUEST, "product not found. productId=%s".formatted(productId));
		}
	}
}
