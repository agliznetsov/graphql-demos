package com.graphql.sample.boot.model;

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
