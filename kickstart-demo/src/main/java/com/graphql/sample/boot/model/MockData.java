package com.graphql.sample.boot.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class MockData {

	private static List<Book> BOOKS = Arrays.asList(
			Book.builder().id("1").name("Harry Potter and the Philosopher's Stone").pageCount(223).authorId("1")
					.build(),
			Book.builder().id("2").name("Moby Dick").pageCount(635).authorId("2").build()
	);

	private static List<Author> AUTHORS = Arrays.asList(
			Author.builder().id("1").firstName("Joanne").lastName("Rowling").build(),
			Author.builder().id("2").firstName("Herman").lastName("Melville").build()
	);

	public static Book findBookById(String bookId) {
		return BOOKS.stream()
				.filter(book -> book.getId().equals(bookId))
				.findFirst()
				.orElse(null);
	}

	public static List<Book> findBooks(FindBookRequest request) {
		Stream<Book> stream = MockData.BOOKS.stream();
		if (request != null && request.getName() != null) {
			stream = stream.filter(book -> book.getName().contains(request.getName()));
		}
		return stream.collect(Collectors.toList());
	}

	public static Author findAuthorById(String authorId) {
		return AUTHORS.stream()
				.filter(author -> author.getId().equals(authorId))
				.findFirst()
				.orElse(null);
	}

	public static Map<Book,Author> getBookAuthors(List<Book> books) {
		return books.stream().collect(Collectors.toMap(it -> it, it -> findAuthorById(it.getAuthorId())));
	}

	public static List<Comment> getBookComments(String bookId) {
		return Arrays.asList(
				Comment.builder().text("comment #1 for book " + bookId).build(),
				Comment.builder().text("comment #2 for book " + bookId).build(),
				Comment.builder().text("comment #3 for book " + bookId).build()
		);
	}
}
