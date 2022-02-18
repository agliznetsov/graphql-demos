package io.spring.sample.graphql.book.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Book implements WithID {
	int id;
	String name;
	int pageCount;
	int authorId;
}
