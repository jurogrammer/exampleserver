package juro.exampleserver.dto.comon;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageableDto<T> {
	private List<T> items;
	// 다음 페이지 호출시 전달할 next. 더 이상 조회할 아이템이 없을 경우 null
	private String searchAfter;
	// 전체 아이템 수
	private Long totalCount;
}
