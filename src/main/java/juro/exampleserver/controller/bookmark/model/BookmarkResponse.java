package juro.exampleserver.controller.bookmark.model;

import java.time.LocalDateTime;

import juro.exampleserver.service.bookmark.model.BookmarkDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResponse {
	private Long id;
	private Long userId;
	private Long productId;
	private LocalDateTime createdAt;

	public static BookmarkResponse of(BookmarkDto dto) {
		return BookmarkResponse.builder()
			.id(dto.getId())
			.userId(dto.getUserId())
			.productId(dto.getProductId())
			.createdAt(dto.getCreatedAt())
			.build();
	}
}
