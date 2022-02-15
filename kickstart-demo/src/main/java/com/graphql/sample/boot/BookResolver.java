package com.graphql.sample.boot;

import java.util.List;

import org.springframework.stereotype.Component;

import com.graphql.sample.boot.model.Author;
import com.graphql.sample.boot.model.Book;
import com.graphql.sample.boot.model.Comment;
import com.graphql.sample.boot.model.MockData;

import graphql.kickstart.tools.GraphQLResolver;

@Component
public class BookResolver implements GraphQLResolver<Book> {

	public Author author(Book book) {
		return MockData.findAuthorById(book.getAuthorId());
	}

	//Batch data loading is not supported
	public List<Comment> comments(Book book) {
		return MockData.getBookComments(book.getId());
	}

}
