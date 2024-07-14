package juro.exampleserver.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;

import juro.exampleserver.exception.ClientException;
import juro.exampleserver.exception.ErrorCode;
import juro.exampleserver.repository.common.PageResult;
import juro.exampleserver.support.model.SearchAfterEncoder;

public class CustomProductRepositoryImpl extends QuerydslRepositorySupport implements CustomProductRepository {

	private final QProduct product = QProduct.product;

	public CustomProductRepositoryImpl() {
		super(Product.class);
	}

	@Override
	public PageResult<Product> search(ProductSearchCriteria criteria) {
		List<Product> products = from(product)
			.where(searchWhereCondition(criteria))
			.limit(criteria.getSize())
			.orderBy(order(criteria.getSort()))
			.fetch();

		String nextSearchAfter = getNextSearchAfter(criteria, products);

		return PageResult.<Product>builder()
			.totalCount(criteria.isWithTotalCount() ? count(criteria) : null)
			.nextSearchAfter(nextSearchAfter)
			.items(products)
			.build();
	}

	private String getNextSearchAfter(ProductSearchCriteria criteria, List<Product> products) {
		String nextSearchAfter = null;
		if (criteria.getSize() == products.size()) {
			switch (criteria.getSort()) {
				case RECENT -> {
					Product lastProduct = products.get(products.size() - 1);
					nextSearchAfter = SearchAfterEncoder.encode(lastProduct.getId().toString());
				}
				case PRICE -> {
					Product lastProduct = products.get(products.size() - 1);
					String nextPrice = lastProduct.getPrice().toString();
					String nextId = lastProduct.getId().toString();

					nextSearchAfter = SearchAfterEncoder.encode(nextPrice, nextId);
				}
			}

		}
		return nextSearchAfter;
	}

	public long count(ProductSearchCriteria criteria) {
		return from(product).where(searchWhereCondition(criteria)).fetchCount();
	}

	private BooleanExpression searchWhereCondition(ProductSearchCriteria criteria) {
		return statusEqual(criteria.getProductStatus())
			.and(userIdEqual(criteria.getUserId()))
			.and(nextProduct(criteria));
	}

	private BooleanExpression statusEqual(ProductStatus status) {
		if (status == null) {
			return null;
		}

		return product.status.eq(status);
	}

	private BooleanExpression userIdEqual(Long userId) {
		if (userId == null) {
			return null;
		}

		return product.userId.eq(userId);
	}

	private BooleanExpression nextProduct(ProductSearchCriteria criteria) {
		if (criteria.getSearchAfter() == null) {
			return null;
		}

		switch (criteria.getSort()) {
			case RECENT -> {
				String decode = SearchAfterEncoder.decodeSingle(criteria.getSearchAfter());
				long lastProductId = Long.parseLong(decode);
				return product.id.lt(lastProductId);

			}
			case PRICE -> {
				String[] decodes = SearchAfterEncoder.decode(criteria.getSearchAfter());
				long lastPrice = Long.parseLong(decodes[0]);
				long lastProductId = Long.parseLong(decodes[1]);

				return product.price.lt(lastPrice).or(product.price.eq(lastPrice).and(product.id.lt(lastProductId)));
			}
			default -> throw new ClientException(ErrorCode.BAD_REQUEST, "search after incorrect. criteria=" + criteria);
		}
	}

	private OrderSpecifier<?>[] order(ProductSortType sortType) {
		return switch (sortType) {
			case RECENT -> new OrderSpecifier<?>[] {product.id.desc()};
			case PRICE -> new OrderSpecifier<?>[] {
				new OrderSpecifier<>(Order.DESC, product.price),
				new OrderSpecifier<>(Order.DESC, product.id)
			};
			default -> throw new ClientException(ErrorCode.INTERNAL_SERVER_ERROR,
				"Invalid sort type. sortType=" + sortType);
		};
	}
}
