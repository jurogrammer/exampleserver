package juro.exampleserver.service.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequestDto {
	private Long userId;
	private String name;
	private Long price;
	private Long quantity;
}
