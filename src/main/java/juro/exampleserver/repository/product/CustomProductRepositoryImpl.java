package juro.exampleserver.repository.product;

import java.util.Collections;

import juro.exampleserver.repository.common.PageResult;

public class CustomProductRepositoryImpl implements CustomProductRepository {

	@Override
	public PageResult<Product> search(ProductSearchCriteria criteria) {

		return PageResult.<Product>builder()
			.totalCount(0L)
			.nextSearchAfter(null)
			.items(Collections.emptyList())
			.build();
	}
}
