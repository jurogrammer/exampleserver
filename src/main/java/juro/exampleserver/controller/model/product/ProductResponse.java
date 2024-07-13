package juro.exampleserver.controller.model.product;

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
	private long id;
	private String name;
	private long price;
	private long quantity;
	private ProductStatus status;

}
