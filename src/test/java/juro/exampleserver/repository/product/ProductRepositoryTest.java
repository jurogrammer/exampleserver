package juro.exampleserver.repository.product;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import juro.exampleserver.repository.common.PageResult;

@DataJpaTest
@Import(CustomProductRepositoryImpl.class)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	public void setUp() {
		productRepository.deleteAll();
		// Insert test data
		productRepository.save(Product.builder()
			.userId(1L)
			.name("Product 1")
			.price(100L)
			.quantity(10L)
			.status(ProductStatus.AVAILABLE)
			.build());
		productRepository.save(Product.builder()
			.userId(2L)
			.name("Product 2")
			.price(200L)
			.quantity(20L)
			.status(ProductStatus.SOLD_OUT)
			.build());
		productRepository.save(Product.builder()
			.userId(1L)
			.name("Product 3")
			.price(300L)
			.quantity(30L)
			.status(ProductStatus.PAUSED)
			.build());
		productRepository.save(Product.builder()
			.userId(2L)
			.name("Product 4")
			.price(400L)
			.quantity(40L)
			.status(ProductStatus.DELETED)
			.build());
		productRepository.save(Product.builder()
			.userId(1L)
			.name("Product 5")
			.price(150L)
			.quantity(15L)
			.status(ProductStatus.AVAILABLE)
			.build());
	}

	@Test
	@DisplayName("search - 구매 가능한 상품 최신순 조회")
	public void testSearchAvailableProducts() {
		ProductSearchCriteria criteria = ProductSearchCriteria.builder()
			.productStatus(ProductStatus.AVAILABLE)
			.userId(1L)
			.size(2)
			.sort(ProductSortType.RECENT)
			.withTotalCount(true)
			.build();

		PageResult<Product> result = productRepository.search(criteria);

		assertThat(result.getItems()).hasSize(2);
		assertThat(result.getTotalCount()).isEqualTo(2);
		assertThat(result.getItems().get(0).getName()).isEqualTo("Product 5");
		assertThat(result.getItems().get(1).getName()).isEqualTo("Product 1");
		assertThat(result.getNextSearchAfter()).isNotNull();
	}

	@Test
	@DisplayName("search - 삭제된 상품 조회")
	public void testSearchDeletedProducts() {
		ProductSearchCriteria criteria = ProductSearchCriteria.builder()
			.productStatus(ProductStatus.DELETED)
			.size(10)
			.sort(ProductSortType.RECENT)
			.withTotalCount(true)
			.build();

		PageResult<Product> result = productRepository.search(criteria);

		assertThat(result.getItems()).hasSize(1);
		assertThat(result.getTotalCount()).isEqualTo(1);
		assertThat(result.getItems().get(0).getName()).isEqualTo("Product 4");
		assertThat(result.getNextSearchAfter()).isNull();
	}

	@Test
	@DisplayName("search - 최신 순 조회")
	public void testSearchWithPaginationAndSorting() {
		ProductSearchCriteria criteria = ProductSearchCriteria.builder()
			.productStatus(ProductStatus.AVAILABLE)
			.size(1)
			.sort(ProductSortType.RECENT)
			.withTotalCount(true)
			.build();

		PageResult<Product> result = productRepository.search(criteria);

		assertThat(result.getItems()).hasSize(1);
		assertThat(result.getTotalCount()).isEqualTo(2);
		assertThat(result.getItems().get(0).getName()).isEqualTo("Product 5");
		assertThat(result.getNextSearchAfter()).isNotNull();

		// Fetch the next page
		criteria.setSearchAfter(result.getNextSearchAfter());
		result = productRepository.search(criteria);

		assertThat(result.getItems()).hasSize(1);
		assertThat(result.getItems().get(0).getName()).isEqualTo("Product 1");
		assertThat(result.getNextSearchAfter()).isNotNull();

		// Fetch the next page
		criteria.setSearchAfter(result.getNextSearchAfter());
		result = productRepository.search(criteria);
		assertThat(result.getItems()).hasSize(0);
		assertThat(result.getNextSearchAfter()).isNull();
	}

	@Test
	@DisplayName("search - 가격 순 조회")
	public void testSearchSortedByPriceWithPagination() {
		ProductSearchCriteria criteria = ProductSearchCriteria.builder()
			.productStatus(ProductStatus.AVAILABLE)
			.size(1)
			.sort(ProductSortType.PRICE)
			.withTotalCount(true)
			.build();

		// First page
		PageResult<Product> result = productRepository.search(criteria);

		assertThat(result.getItems()).hasSize(1);
		assertThat(result.getTotalCount()).isEqualTo(2); // Assuming there are 2 available products
		assertThat(result.getItems().get(0).getName()).isEqualTo("Product 5"); // Cheapest product
		assertThat(result.getNextSearchAfter()).isNotNull();

		// Second page
		criteria.setSearchAfter(result.getNextSearchAfter());
		result = productRepository.search(criteria);

		assertThat(result.getItems()).hasSize(1);
		assertThat(result.getItems().get(0).getName()).isEqualTo("Product 1"); // Next cheapest product
		assertThat(result.getNextSearchAfter()).isNotNull(); // No more records after this page

		// third page
		criteria.setSearchAfter(result.getNextSearchAfter());
		result = productRepository.search(criteria);

		assertThat(result.getItems()).hasSize(0);
		assertThat(result.getNextSearchAfter()).isNull(); // No more records after this page

	}
}
