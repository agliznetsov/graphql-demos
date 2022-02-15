package com.graphql.sample.boot;

import java.util.List;

import org.springframework.stereotype.Component;

import com.graphql.sample.boot.model.Book;
import com.graphql.sample.boot.model.FindBookRequest;
import com.graphql.sample.boot.model.MockData;

import graphql.kickstart.tools.GraphQLQueryResolver;

@Component
class QueryResolver implements GraphQLQueryResolver {

	public List<Book> books(FindBookRequest request) {
		return MockData.findBooks(request);
	}

	public Book bookById(String id) {
		return MockData.findBookById(id);
	}

}
