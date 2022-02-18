package io.spring.sample.graphql.book.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QueryUtils {
	public static <T extends WithID> DataPage<T> getPage(Stream<T> stream, PaginationParameters pagination) {
		if (pagination.isReversed()) {
			stream = stream.sorted(Comparator.comparing(WithID::getId).reversed());
		}
		if (pagination.getCursor() != null) {
			int id = Integer.parseInt(pagination.getCursor());
			if (pagination.isReversed()) {
				stream = stream.filter(it -> it.getId() < id);
			} else {
				stream = stream.filter(it -> it.getId() > id);
			}
		}
		if (pagination.getLimit() != null) {
			stream = stream.limit(pagination.getLimit());
		}
		List<T> data = stream.collect(Collectors.toList());
		if (pagination.isReversed()) {
			Collections.reverse(data);
		}
		if (pagination.getLimit() != null && data.size() > pagination.getLimit()) {
			return DataPage.<T>builder()
					.data(data.stream().limit(pagination.getLimit()).collect(Collectors.toList()))
					.nextPageExists(true)
					.reversed(pagination.isReversed())
					.build();
		} else {
			return DataPage.<T>builder()
					.data(data)
					.reversed(pagination.isReversed())
					.build();
		}
	}
}
