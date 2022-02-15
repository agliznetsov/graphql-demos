package com.demo.graphql.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Author {
	String id;
	String firstName;
	String lastName;
}
