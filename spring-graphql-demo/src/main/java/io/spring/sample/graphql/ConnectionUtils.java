package io.spring.sample.graphql;

import graphql.relay.DefaultConnectionCursor;
import io.spring.sample.graphql.book.model.PaginationParameters;

public abstract class ConnectionUtils {
	public static PaginationParameters toPaginationParameters(Integer first,
	                                                    DefaultConnectionCursor after,
	                                                    Integer last,
	                                                    DefaultConnectionCursor before) {
		PaginationParameters.PaginationParametersBuilder builder = PaginationParameters.builder();
		if (before != null) {
			builder.cursor(before.getValue());
			builder.reversed(true);
		} else if (after != null){
			builder.cursor(after.getValue());
		}
		if (first != null) {
			builder.limit(first);
		} else if (last != null) {
			builder.limit(last);
		}
		return builder.build();
	}
}
