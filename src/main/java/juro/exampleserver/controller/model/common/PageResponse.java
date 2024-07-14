package juro.exampleserver.controller.model.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
	private List<T> items;
	private String nextSearchAfter;
	private Long totalCount;
}
