package io.spring.sample.graphql.book.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book {
	String id;
	String name;
	int pageCount;
	String authorId;
}
