package juro.exampleserver.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import juro.exampleserver.config.LoginUser;
import juro.exampleserver.config.model.ServiceUser;
import juro.exampleserver.controller.model.bookmark.BookmarkResponse;
import juro.exampleserver.controller.model.common.ApiResponse;
import juro.exampleserver.dto.bookmark.BookmarkDto;
import juro.exampleserver.service.BookmarkService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BookmarkController {

	private final BookmarkService bookmarkService;

	@LoginUser
	@GetMapping("/v1/bookmarks")
	public ApiResponse<List<BookmarkResponse>> getBookmarks(
		@AuthenticationPrincipal ServiceUser serviceUser
	) {
		List<BookmarkResponse> response = bookmarkService.getBookmarks(serviceUser.getId())
			.stream()
			.map(BookmarkResponse::of)
			.toList();

		return ApiResponse.success(response);
	}

	@LoginUser
	@PostMapping("/v1/products/{productId}/bookmarks")
	public ApiResponse<BookmarkResponse> addBookmark(
		@AuthenticationPrincipal ServiceUser serviceUser,
		@PathVariable Long productId
	) {
		BookmarkDto bookmarkDto = bookmarkService.registerBookmark(serviceUser.getId(), productId);
		BookmarkResponse response = BookmarkResponse.of(bookmarkDto);

		return ApiResponse.success(response);
	}

	@LoginUser
	@DeleteMapping("/v1/products/{productId}/bookmarks")
	public ApiResponse<Void> removeBookmark(
		@AuthenticationPrincipal ServiceUser serviceUser,
		@PathVariable Long productId
	) {
		bookmarkService.removeBookmark(serviceUser.getId(), productId);

		return ApiResponse.success();
	}

}
