package io.spring.sample.graphql.book.model;

import graphql.relay.ConnectionCursor;
import graphql.relay.Edge;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorEdge implements Edge<Author> {
	private Author node;
	private ConnectionCursor cursor;
}
