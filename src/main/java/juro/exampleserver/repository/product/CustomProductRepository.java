package juro.exampleserver.repository.product;

import juro.exampleserver.repository.common.PageResult;

public interface CustomProductRepository {

	PageResult<Product> search(ProductSearchCriteria criteria);
}
