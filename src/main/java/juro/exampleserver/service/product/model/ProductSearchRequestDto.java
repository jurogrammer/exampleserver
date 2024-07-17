package juro.exampleserver.service.product.model;

import juro.exampleserver.repository.product.ProductSearchCriteria;
import juro.exampleserver.repository.product.ProductSortType;
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
	private String searchAfter;
	private ProductSortType sort;
	private boolean withTotalCount;

	public ProductSearchCriteria toCriteria() {
		return ProductSearchCriteria.builder()
			.productStatus(productStatus)
			.userId(userId)
			.size(size)
			.sort(sort)
			.searchAfter(searchAfter)
			.withTotalCount(withTotalCount)
			.build();
	}
}
