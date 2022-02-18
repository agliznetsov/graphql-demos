package io.spring.sample.graphql.book.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class MockData {
	private static Random random = new Random();

	private static List<Book> BOOKS;
	private static List<Author> AUTHORS;

	static {
		AUTHORS = new ArrayList<>();
		AUTHORS.add(Author.builder().id(1).firstName("Joanne").lastName("Rowling").build());
		AUTHORS.add(Author.builder().id(2).firstName("Herman").lastName("Melville").build());

		BOOKS = new ArrayList<>();
		BOOKS.add(Book.builder().id(1).name("Harry Potter and the Philosopher's Stone").pageCount(223).authorId(1)
				.build());
		BOOKS.add(Book.builder().id(2).name("Moby Dick").pageCount(635).authorId(2).build());

		int bookId = 3;
		for (int i = 3; i <= 100; i++) {
			AUTHORS.add(Author.builder().id(i).firstName(randomName()).lastName(randomName()).build());
			for (int n = 0; n < 5; n++) {
				BOOKS.add(Book.builder().id(bookId).name(randomName()).pageCount(randomInt())
						.authorId(i)
						.build());
				bookId++;
			}
		}
	}

	private static int randomInt() {
		return random.nextInt(500);
	}

	private static String randomName() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static Book findBookById(int bookId) {
		return BOOKS.stream()
				.filter(book -> book.getId() == bookId)
				.findFirst()
				.orElse(null);
	}

	public static DataPage<Book> findBooks(FindBookRequest request, PaginationParameters pagination) {
		Stream<Book> stream = MockData.BOOKS.stream();
		if (request != null && request.getName() != null) {
			stream = stream.filter(book -> book.getName().contains(request.getName()));
		}
		return QueryUtils.getPage(stream, pagination);
	}

	public static DataPage<Author> findAuthors(FindAuthorRequest request, PaginationParameters pagination) {
		Stream<Author> stream = MockData.AUTHORS.stream();
		if (request != null && request.getName() != null) {
			stream = stream.filter(author -> author.getLastName().contains(request.getName()));
		}
		return QueryUtils.getPage(stream, pagination);
	}

	public static Author findAuthorById(int authorId) {
		return AUTHORS.stream()
				.filter(author -> author.getId() == authorId)
				.findFirst()
				.orElse(null);
	}

	public static Map<Book,Author> getBookAuthors(List<Book> books) {
		return books.stream().collect(Collectors.toMap(it -> it, it -> findAuthorById(it.getAuthorId())));
	}

	public static Map<Book,List<Comment>> getBookComments(List<Book> books) {
		return books.stream()
				.collect(Collectors.toMap(it -> it, it -> Arrays.asList(
						Comment.builder().text("comment #1 for book " + it.getId()).build(),
						Comment.builder().text("comment #2 for book " + it.getId()).build(),
						Comment.builder().text("comment #3 for book " + it.getId()).build()
				)));
	}

}
