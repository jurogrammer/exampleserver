package juro.exampleserver.controller.model.product;

import juro.exampleserver.dto.product.ProductDto;
import juro.exampleserver.repository.product.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
	private Long id;
	private Long userId;
	private String name;
	private Long price;
	private Long quantity;
	private ProductStatus status;

	public static ProductResponse of(ProductDto product) {
		return ProductResponse.builder()
			.id(product.getId())
			.userId(product.getUserId())
			.name(product.getName())
			.price(product.getPrice())
			.quantity(product.getQuantity())
			.status(product.getStatus())
			.build();
	}
}
