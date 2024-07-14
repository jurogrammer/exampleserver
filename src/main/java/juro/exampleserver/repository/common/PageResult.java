package juro.exampleserver.repository.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
	private List<T> items;
	private String nextSearchAfter;
	private Long totalCount;
}
