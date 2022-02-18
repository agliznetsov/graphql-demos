package io.spring.sample.graphql.book.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Author implements WithID {
	int id;
	String firstName;
	String lastName;
}
