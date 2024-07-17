package juro.exampleserver.dto.bookmark;

import java.time.LocalDateTime;

import juro.exampleserver.repository.bookmark.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDto {
	private Long id;
	private Long userId;
	private Long productId;
	private LocalDateTime createdAt;

	public static BookmarkDto of(Bookmark bookmark) {
		return BookmarkDto.builder()
			.id(bookmark.getId())
			.userId(bookmark.getUserId())
			.productId(bookmark.getProductId())
			.createdAt(bookmark.getCreatedAt())
			.build();
	}
}
