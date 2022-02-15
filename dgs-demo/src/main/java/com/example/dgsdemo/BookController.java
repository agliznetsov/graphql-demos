package com.example.dgsdemo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;

import com.example.dgsdemo.model.Author;
import com.example.dgsdemo.model.Book;
import com.example.dgsdemo.model.Comment;
import com.example.dgsdemo.model.FindBookRequest;
import com.example.dgsdemo.model.MockData;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsData;
import com.netflix.graphql.dgs.DgsDataFetchingEnvironment;
import com.netflix.graphql.dgs.DgsDataLoader;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;

@DgsComponent
public class BookController {

	//top-level query data fetcher
	@DgsQuery
	public List<Book> books(@InputArgument FindBookRequest request) {
		return MockData.findBooks(request);
	}

	//batch data loader to be used in other queries
	@DgsDataLoader(name = "author")
	BatchLoader<String,Author> authorBatchLoader = (keys) -> {
		return CompletableFuture.supplyAsync(() -> MockData.getAuthors(keys));
	};

	//Book.author data fetcher that uses batch data loader defined above
	@DgsData(parentType = "Book")
	public CompletableFuture<Author> author(DgsDataFetchingEnvironment dfe) {
		DataLoader<String,Author> dataLoader = dfe.getDataLoader("author");
		Book book = dfe.getSource();
		return dataLoader.load(book.getAuthorId());
	}

	@DgsDataLoader(name = "comments")
	BatchLoader<String, List<Comment>> commentsBatchLoader = (keys) -> {
		return CompletableFuture.supplyAsync(() -> MockData.getBookComments(keys));
	};

	@DgsData(parentType = "Book")
	public CompletableFuture<Author> comments(DgsDataFetchingEnvironment dfe) {
		DataLoader<String, Author> dataLoader = dfe.getDataLoader("comments");
		Book book = dfe.getSource();
		return dataLoader.load(book.getId());
	}

}
