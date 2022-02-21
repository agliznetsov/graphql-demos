package com.example.bookdemo;


import static com.example.bookdemo.model.ConnectionUtils.toPaginationParameters;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.bookdemo.apis.QueryApi;
import com.example.bookdemo.model.*;

import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.DefaultPageInfo;


@Controller
public class QueryApiImpl implements QueryApi {

	@Override
	public Book bookById(@Argument Integer id) {
		return MockData.findBookById(id);
	}

	@Override
	public BookConnection books(@Argument FindBookRequest request,
	                            @Argument Integer first,
	                            @Argument String after,
	                            @Argument Integer last,
	                            @Argument String before) {
		PaginationParameters pagination = toPaginationParameters(first, after, last, before);
		DataPage<Book> page = MockData.findBooks(request, pagination);
		return toConnection(page);
	}

	private BookConnection toConnection(DataPage<Book> page) {
		 BookConnection.Builder builder = BookConnection.newBuilder()
				.edges(toEdges(page.getData()))
                .pageInfo(new DefaultPageInfo(
                        toCursor(page.getFirst()),
                        toCursor(page.getLast()),
                        true,
		                true));
		return builder.build();
	}

	@QueryMapping
	public AuthorConnection authors(@Argument FindAuthorRequest request,
	                       @Argument Integer first,
	                       @Argument String after,
	                       @Argument Integer last,
	                       @Argument String before) {
		PaginationParameters pagination = toPaginationParameters(first, after, last, before);
		DataPage<Author> page = MockData.findAuthors(request, pagination);
		return toAuthorConnection(page);
	}

	private AuthorConnection toAuthorConnection(DataPage<Author> page) {
		AuthorConnection.Builder builder = AuthorConnection.newBuilder()
				.edges(toAuthorEdges(page.getData()))
				.pageInfo(new DefaultPageInfo(
						toCursor(page.getFirst()),
						toCursor(page.getLast()),
						true,
						true));
		return builder.build();
	}

	private List<AuthorEdge> toAuthorEdges(List<Author> authors) {
		return authors.stream().map(it -> AuthorEdge.newBuilder()
						.node(it)
						.cursor(String.valueOf(it.getId()))
						.build())
				.collect(Collectors.toList());
	}


	private List<BookEdge> toEdges(List<Book> books) {
		return books.stream().map(it -> BookEdge.newBuilder()
						.node(it)
						.cursor(String.valueOf(it.getId()))
						.build())
				.collect(Collectors.toList());
	}

	private ConnectionCursor toCursor(WithID it) {
		return it != null ? new DefaultConnectionCursor(String.valueOf(it.getId())) : null;
	}

}
