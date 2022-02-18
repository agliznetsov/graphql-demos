package io.spring.sample.graphql.book.model;

import graphql.relay.ConnectionCursor;
import graphql.relay.Edge;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookEdge implements Edge<Book> {
	private Book node;
	private ConnectionCursor cursor;
}
