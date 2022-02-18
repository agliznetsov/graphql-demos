package io.spring.sample.graphql.book.model;

import java.util.List;

import graphql.relay.Connection;
import graphql.relay.Edge;
import graphql.relay.PageInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookConnection implements Connection<Book> {
	private List<Edge<Book>> edges;
	private PageInfo pageInfo;
}
