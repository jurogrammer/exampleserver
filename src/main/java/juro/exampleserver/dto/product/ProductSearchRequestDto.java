package juro.exampleserver.dto.product;

import juro.exampleserver.repository.product.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequestDto {
	private ProductStatus productStatus;
	private Long userId;
	private Integer size;
	private String next;
}
