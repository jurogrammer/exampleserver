package juro.exampleserver.controller.model.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductCreateRequest {
	@NotBlank
	private String name;
	@Min(0)
	private Long price;
	@Min(0)
	private Long quantity;
}
