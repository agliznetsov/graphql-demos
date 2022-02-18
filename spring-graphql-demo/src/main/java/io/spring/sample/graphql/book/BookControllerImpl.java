package io.spring.sample.graphql.book;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;

import io.spring.sample.graphql.book.generated.BookController;
import io.spring.sample.graphql.book.model.Author;
import io.spring.sample.graphql.book.model.Book;
import io.spring.sample.graphql.book.model.Comment;
import io.spring.sample.graphql.book.model.MockData;
import reactor.core.publisher.Mono;

@Controller
public class BookControllerImpl implements BookController {

	@Override
	public Mono<Map<Book, Author>> author(List<Book> books) {
		return Mono.just(MockData.getBookAuthors(books));
	}

	@Override
	public Mono<Map<Book, List<Comment>>> comments(List<Book> books) {
		return Mono.just(MockData.getBookComments(books));
	}

}
