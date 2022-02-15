package io.spring.sample.graphql.book;

import java.util.List;
import java.util.Map;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import io.spring.sample.graphql.book.model.Author;
import io.spring.sample.graphql.book.model.Book;
import io.spring.sample.graphql.book.model.Comment;
import io.spring.sample.graphql.book.model.FindBookRequest;
import io.spring.sample.graphql.book.model.MockData;
import reactor.core.publisher.Mono;

@Controller
public class BookController {

	@QueryMapping
	public Book bookById(@Argument String id) {
		return MockData.findBookById(id);
	}

	@QueryMapping
	public List<Book> books(@Argument FindBookRequest request) {
		return MockData.findBooks(request);
	}

	@SchemaMapping
	public Author author(Book book) {
		return MockData.findAuthorById(book.getAuthorId());
	}

//	@BatchMapping
//	public Mono<Map<Book, Author>> author(List<Book> books) {
//		return Mono.just(MockData.getBookAuthors(books));
//	}

	@BatchMapping
	public Mono<Map<Book, List<Comment>>> comments(List<Book> books) {
		return Mono.just(MockData.getBookComments(books));
	}

}
