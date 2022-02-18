package io.spring.sample.graphql.book;

import static io.spring.sample.graphql.ConnectionUtils.toPaginationParameters;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.DefaultPageInfo;
import graphql.relay.Edge;
import io.spring.sample.graphql.book.generated.QueryController;
import io.spring.sample.graphql.book.model.Author;
import io.spring.sample.graphql.book.model.AuthorConnection;
import io.spring.sample.graphql.book.model.AuthorEdge;
import io.spring.sample.graphql.book.model.Book;
import io.spring.sample.graphql.book.model.BookConnection;
import io.spring.sample.graphql.book.model.BookEdge;
import io.spring.sample.graphql.book.model.DataPage;
import io.spring.sample.graphql.book.model.FindAuthorRequest;
import io.spring.sample.graphql.book.model.FindBookRequest;
import io.spring.sample.graphql.book.model.MockData;
import io.spring.sample.graphql.book.model.PaginationParameters;
import io.spring.sample.graphql.book.model.WithID;

@Controller
public class QueryControllerImpl implements QueryController {

	@Override
	public Book bookById(@Argument Integer id) {
		return MockData.findBookById(id);
	}

	@Override
	public BookConnection books(@Argument FindBookRequest request,
	                            @Argument Integer first,
	                            @Argument DefaultConnectionCursor after,
	                            @Argument Integer last,
	                            @Argument DefaultConnectionCursor before) {
		PaginationParameters pagination = toPaginationParameters(first, after, last, before);
		DataPage<Book> page = MockData.findBooks(request, pagination);
		return toConnection(page);
	}

	private BookConnection toConnection(DataPage<Book> page) {
		 BookConnection.BookConnectionBuilder builder = BookConnection.builder()
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
	                       @Argument DefaultConnectionCursor after,
	                       @Argument Integer last,
	                       @Argument DefaultConnectionCursor before) {
		PaginationParameters pagination = toPaginationParameters(first, after, last, before);
		DataPage<Author> page = MockData.findAuthors(request, pagination);
		return toAuthorConnection(page);
	}

	private AuthorConnection toAuthorConnection(DataPage<Author> page) {
		AuthorConnection.AuthorConnectionBuilder builder = AuthorConnection.builder()
				.edges(toAuthorEdges(page.getData()))
				.pageInfo(new DefaultPageInfo(
						toCursor(page.getFirst()),
						toCursor(page.getLast()),
						true,
						true));
		return builder.build();
	}

	private List<Edge<Author>> toAuthorEdges(List<Author> authors) {
		return authors.stream().map(it -> AuthorEdge.builder()
						.node(it)
						.cursor(new DefaultConnectionCursor(String.valueOf(it.getId())))
						.build())
				.collect(Collectors.toList());
	}


	private List<Edge<Book>> toEdges(List<Book> books) {
		return books.stream().map(it -> BookEdge.builder()
						.node(it)
						.cursor(new DefaultConnectionCursor(String.valueOf(it.getId())))
						.build())
				.collect(Collectors.toList());
	}

	private ConnectionCursor toCursor(WithID it) {
		return it != null ? new DefaultConnectionCursor(String.valueOf(it.getId())) : null;
	}

}
