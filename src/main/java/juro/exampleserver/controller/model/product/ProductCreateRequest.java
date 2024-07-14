package juro.exampleserver.controller.model.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import juro.exampleserver.dto.product.ProductCreateRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {
	@NotBlank
	private String name;
	@Min(0)
	private Long price;
	@Min(0)
	private Long quantity;

	public ProductCreateRequestDto toDto(Long userId) {
		return ProductCreateRequestDto.builder()
			.userId(userId)
			.name(name)
			.price(price)
			.quantity(quantity)
			.build();
	}
}
