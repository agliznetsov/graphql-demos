package io.spring.sample.graphql.book.generated;

import java.util.List;
import java.util.Map;

import org.springframework.graphql.data.method.annotation.BatchMapping;

import io.spring.sample.graphql.book.model.Author;
import io.spring.sample.graphql.book.model.Book;
import io.spring.sample.graphql.book.model.Comment;
import reactor.core.publisher.Mono;

public interface BookController {

	@BatchMapping
	Mono<Map<Book, Author>> author(List<Book> books);

	@BatchMapping
	Mono<Map<Book, List<Comment>>> comments(List<Book> books);

}
