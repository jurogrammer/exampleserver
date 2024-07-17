package juro.exampleserver.service.bookmark;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import juro.exampleserver.service.bookmark.model.BookmarkDto;
import juro.exampleserver.exception.ClientException;
import juro.exampleserver.exception.ErrorCode;
import juro.exampleserver.repository.bookmark.Bookmark;
import juro.exampleserver.repository.bookmark.BookmarkRepository;
import juro.exampleserver.service.user.UserValidator;
import juro.exampleserver.service.product.ProductValidator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkService {

	private final BookmarkRepository bookmarkRepository;
	private final UserValidator userValidator;
	private final ProductValidator productValidator;

	@Transactional
	public BookmarkDto registerBookmark(Long userId, Long productId) {
		userValidator.validateUserExists(userId);
		productValidator.validateProductExists(productId);

		bookmarkRepository.findByUserIdAndProductId(userId, productId)
			.ifPresent(mark -> {
				throw new ClientException(ErrorCode.BAD_REQUEST,
					"already bookmarked. userId=%s, productId=%s".formatted(userId, productId));
			});

		Bookmark savedBookmark = bookmarkRepository.save(Bookmark.create(userId, productId));

		return BookmarkDto.of(savedBookmark);

	}

	@Transactional
	public void removeBookmark(Long userId, Long productId) {
		userValidator.validateUserExists(userId);
		productValidator.validateProductExists(productId);

		Bookmark bookmark = bookmarkRepository.findByUserIdAndProductId(userId, productId)
			.orElseThrow(() -> new ClientException(ErrorCode.BAD_REQUEST,
				"bookmark not found. userId=%s, productId=%s".formatted(userId, productId)));

		bookmarkRepository.delete(bookmark);
	}

	@Transactional(readOnly = true)
	public List<BookmarkDto> getBookmarks(Long userId) {
		userValidator.validateUserExists(userId);

		return bookmarkRepository.findByUserId(userId).stream().map(BookmarkDto::of).toList();
	}
}
