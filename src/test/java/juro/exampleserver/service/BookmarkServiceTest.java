package juro.exampleserver.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import juro.exampleserver.service.bookmark.model.BookmarkDto;
import juro.exampleserver.exception.ClientException;
import juro.exampleserver.exception.ErrorCode;
import juro.exampleserver.repository.bookmark.Bookmark;
import juro.exampleserver.repository.bookmark.BookmarkRepository;
import juro.exampleserver.service.product.ProductValidator;
import juro.exampleserver.service.bookmark.BookmarkService;
import juro.exampleserver.service.user.UserValidator;

@ActiveProfiles("local")
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {BookmarkService.class})
public class BookmarkServiceTest {

	@MockBean
	private BookmarkRepository bookmarkRepository;

	@MockBean
	private UserValidator userValidator;

	@MockBean
	private ProductValidator productValidator;

	@Autowired
	private BookmarkService bookmarkService;

	private Long userId;
	private Long productId;
	private Bookmark bookmark;

	@BeforeEach
	public void setUp() {
		userId = 1L;
		productId = 1L;

		bookmark = Bookmark.builder()
			.userId(userId)
			.productId(productId)
			.build();
	}

	@Test
	public void testRegisterBookmark_Success() {
		when(bookmarkRepository.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.empty());
		when(bookmarkRepository.save(any(Bookmark.class))).thenReturn(bookmark);

		BookmarkDto result = bookmarkService.registerBookmark(userId, productId);

		assertThat(result).isNotNull();
		assertThat(result.getUserId()).isEqualTo(userId);
		assertThat(result.getProductId()).isEqualTo(productId);
		verify(userValidator, times(1)).validateUserExists(userId);
		verify(productValidator, times(1)).validateProductExists(productId);
		verify(bookmarkRepository, times(1)).findByUserIdAndProductId(userId, productId);
		verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
	}

	@Test
	public void testRegisterBookmark_UserNotFound() {

		doThrow(new ClientException(ErrorCode.BAD_REQUEST, "user not found. userId=" + userId))
			.when(userValidator).validateUserExists(userId);

		ClientException exception = assertThrows(ClientException.class, () ->
			bookmarkService.registerBookmark(userId, productId));

		assertThat(exception.getMessage()).contains("user not found");
		verify(userValidator, times(1)).validateUserExists(userId);
		verify(productValidator, times(0)).validateProductExists(productId);
		verify(bookmarkRepository, times(0)).findByUserIdAndProductId(userId, productId);
		verify(bookmarkRepository, times(0)).save(any(Bookmark.class));
	}

	@Test
	public void testRegisterBookmark_ProductNotFound() {

		doNothing().when(userValidator).validateUserExists(userId);
		doThrow(new ClientException(ErrorCode.BAD_REQUEST, "product not found. productId=" + productId))
			.when(productValidator).validateProductExists(productId);

		ClientException exception = assertThrows(ClientException.class, () ->
			bookmarkService.registerBookmark(userId, productId));

		assertThat(exception.getMessage()).contains("product not found");
		verify(userValidator, times(1)).validateUserExists(userId);
		verify(productValidator, times(1)).validateProductExists(productId);
		verify(bookmarkRepository, times(0)).findByUserIdAndProductId(userId, productId);
		verify(bookmarkRepository, times(0)).save(any(Bookmark.class));
	}

	@Test
	public void testRegisterBookmark_AlreadyBookmarked() {
		when(bookmarkRepository.findByUserIdAndProductId(userId, productId))
			.thenReturn(Optional.of(bookmark));

		ClientException exception = assertThrows(ClientException.class, () ->
			bookmarkService.registerBookmark(userId, productId));

		assertThat(exception.getMessage()).contains("already bookmarked");
		verify(userValidator, times(1)).validateUserExists(userId);
		verify(productValidator, times(1)).validateProductExists(productId);
		verify(bookmarkRepository, times(1)).findByUserIdAndProductId(userId, productId);
		verify(bookmarkRepository, times(0)).save(any(Bookmark.class));
	}

	@Test
	public void testRemoveBookmark_Success() {
		when(bookmarkRepository.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.of(bookmark));

		bookmarkService.removeBookmark(userId, productId);

		verify(userValidator, times(1)).validateUserExists(userId);
		verify(productValidator, times(1)).validateProductExists(productId);
		verify(bookmarkRepository, times(1)).findByUserIdAndProductId(userId, productId);
		verify(bookmarkRepository, times(1)).delete(bookmark);
	}

	@Test
	public void testRemoveBookmark_NotFound() {
		when(bookmarkRepository.findByUserIdAndProductId(userId, productId)).thenReturn(Optional.empty());

		ClientException exception = assertThrows(ClientException.class, () ->
			bookmarkService.removeBookmark(userId, productId));

		assertThat(exception.getMessage()).contains("bookmark not found");
		verify(userValidator, times(1)).validateUserExists(userId);
		verify(productValidator, times(1)).validateProductExists(productId);
		verify(bookmarkRepository, times(1)).findByUserIdAndProductId(userId, productId);
		verify(bookmarkRepository, times(0)).delete(any(Bookmark.class));
	}

	@Test
	public void testGetBookmarks_Success() {
		when(bookmarkRepository.findByUserId(userId)).thenReturn(Collections.singletonList(bookmark));

		List<BookmarkDto> bookmarks = bookmarkService.getBookmarks(userId);

		assertThat(bookmarks).hasSize(1);
		assertThat(bookmarks.get(0).getUserId()).isEqualTo(userId);
		verify(userValidator, times(1)).validateUserExists(userId);
		verify(bookmarkRepository, times(1)).findByUserId(userId);
	}

	@Test
	public void testGetBookmarks_UserNotFound() {
		doThrow(new ClientException(ErrorCode.BAD_REQUEST, "user not found. userId=" + userId))
			.when(userValidator).validateUserExists(userId);

		ClientException exception = assertThrows(ClientException.class, () ->
			bookmarkService.getBookmarks(userId));

		assertThat(exception.getMessage()).contains("user not found");
		verify(userValidator, times(1)).validateUserExists(userId);
		verify(bookmarkRepository, times(0)).findByUserId(userId);
	}
}
