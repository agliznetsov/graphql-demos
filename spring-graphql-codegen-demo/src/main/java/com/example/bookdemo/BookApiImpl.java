package com.example.bookdemo;

import java.util.List;
import java.util.Map;

import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import com.example.bookdemo.apis.BookApi;
import com.example.bookdemo.model.Author;
import com.example.bookdemo.model.Book;
import com.example.bookdemo.model.Comment;
import com.example.bookdemo.model.MockData;

import graphql.GraphQLContext;

@Controller
public class BookApiImpl implements BookApi {

//	@Override
//	public Map<Book,Author> author(List<Book> books) {
//		return MockData.getBookAuthors(books);
//	}

	@Override
	public Author author(Book parent) {
		return MockData.findAuthorById(parent.getAuthorId());
	}

	@Override
	public Map<Book, List<Comment>> comments(List<Book> books) {
		return MockData.getBookComments(books);
	}

}
