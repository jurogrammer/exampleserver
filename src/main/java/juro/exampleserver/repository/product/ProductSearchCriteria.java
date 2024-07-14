package juro.exampleserver.repository.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearchCriteria {
	private ProductStatus productStatus;
	private Long userId;
	private Integer size;
	private String searchAfter;
	private ProductSortType sort;
	private boolean withTotalCount;
}
