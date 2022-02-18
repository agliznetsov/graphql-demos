package io.spring.sample.graphql.book.generated;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;

import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;
import io.spring.sample.graphql.book.model.AuthorConnection;
import io.spring.sample.graphql.book.model.Book;
import io.spring.sample.graphql.book.model.BookConnection;
import io.spring.sample.graphql.book.model.FindAuthorRequest;
import io.spring.sample.graphql.book.model.FindBookRequest;

public interface QueryController {

	@QueryMapping
	Book bookById(@Argument Integer id);

	@QueryMapping
	BookConnection books(@Argument FindBookRequest request,
	                     @Argument Integer first,
	                     @Argument DefaultConnectionCursor after,
	                     @Argument Integer last,
	                     @Argument DefaultConnectionCursor before);

	@QueryMapping
	AuthorConnection authors(@Argument FindAuthorRequest request,
	                         @Argument Integer first,
	                         @Argument DefaultConnectionCursor after,
	                         @Argument Integer last,
	                         @Argument DefaultConnectionCursor before);

}
