package juro.exampleserver.service.product.model;

import juro.exampleserver.repository.product.Product;
import juro.exampleserver.repository.product.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
	private Long id;
	private Long userId;
	private String name;
	private Long price;
	private Long quantity;
	private ProductStatus status;

	public static ProductDto of(Product savedProduct) {
		return ProductDto.builder()
			.id(savedProduct.getId())
			.userId(savedProduct.getUserId())
			.name(savedProduct.getName())
			.price(savedProduct.getPrice())
			.quantity(savedProduct.getQuantity())
			.status(savedProduct.getStatus())
			.build();
	}
}
