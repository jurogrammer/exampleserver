package juro.exampleserver.repository.bookmark;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import juro.exampleserver.config.jpa.JpaConfig;
import juro.exampleserver.repository.product.Product;
import juro.exampleserver.repository.product.ProductRepository;
import juro.exampleserver.repository.user.UserRepository;
import juro.exampleserver.repository.user.model.User;

@DataJpaTest
@ActiveProfiles("local")
@Import(JpaConfig.class)
public class BookmarkRepositoryTest {

	@Autowired
	private BookmarkRepository bookmarkRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	private User user;
	private Product product;

	@BeforeEach
	public void setUp() {
		user = User.builder()
			.username("testuser")
			.password("password")
			.build();
		user = userRepository.save(user);

		product = Product.builder()
			.userId(user.getId())
			.name("Test Product")
			.price(100L)
			.quantity(100L)
			.build();
		product = productRepository.save(product);
	}

	@Test
	public void testAddBookmark() {
		Bookmark bookmark = Bookmark.builder()
			.userId(user.getId())
			.productId(product.getId())
			.build();
		Bookmark savedBookmark = bookmarkRepository.save(bookmark);

		assertThat(savedBookmark.getId()).isNotNull();
		assertThat(savedBookmark.getUserId()).isEqualTo(user.getId());
		assertThat(savedBookmark.getProductId()).isEqualTo(product.getId());
		assertThat(savedBookmark.getCreatedAt()).isNotNull();
	}

	@Test
	public void testFindByUser() {
		Bookmark bookmark = Bookmark.builder()
			.userId(user.getId())
			.productId(product.getId())
			.build();
		bookmarkRepository.save(bookmark);

		List<Bookmark> bookmarks = bookmarkRepository.findByUserId(user.getId());
		assertThat(bookmarks).hasSize(1);
		assertThat(bookmarks.get(0).getProductId()).isEqualTo(product.getId());
	}

	@Test
	public void testFindByUserAndProduct() {
		Bookmark bookmark = Bookmark.builder()
			.userId(user.getId())
			.productId(product.getId())
			.build();
		bookmarkRepository.save(bookmark);

		Optional<Bookmark> foundBookmark = bookmarkRepository.findByUserIdAndProductId(user.getId(), product.getId());
		assertThat(foundBookmark).isPresent();
		assertThat(foundBookmark.get().getUserId()).isEqualTo(user.getId());
		assertThat(foundBookmark.get().getProductId()).isEqualTo(product.getId());
	}

	@Test
	public void testRemoveBookmark() {
		Bookmark bookmark = Bookmark.builder()
			.userId(user.getId())
			.productId(product.getId())
			.build();
		bookmark = bookmarkRepository.save(bookmark);

		bookmarkRepository.delete(bookmark);

		Optional<Bookmark> foundBookmark = bookmarkRepository.findById(bookmark.getId());
		assertThat(foundBookmark).isNotPresent();
	}
}
