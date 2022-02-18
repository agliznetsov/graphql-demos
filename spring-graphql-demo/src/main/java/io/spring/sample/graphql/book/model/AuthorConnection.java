package io.spring.sample.graphql.book.model;

import java.util.List;

import graphql.relay.Connection;
import graphql.relay.Edge;
import graphql.relay.PageInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorConnection implements Connection<Author> {
	private List<Edge<Author>> edges;
	private PageInfo pageInfo;
}
