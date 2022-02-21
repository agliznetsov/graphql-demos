package com.example.bookdemo.model;

public abstract class ConnectionUtils {
	public static PaginationParameters toPaginationParameters(Integer first,
	                                                    String after,
	                                                    Integer last,
	                                                    String before) {
		PaginationParameters.PaginationParametersBuilder builder = PaginationParameters.builder();
		if (before != null) {
			builder.cursor(before);
			builder.reversed(true);
		} else if (after != null){
			builder.cursor(after);
		}
		if (first != null) {
			builder.limit(first);
		} else if (last != null) {
			builder.limit(last);
		}
		return builder.build();
	}
}
