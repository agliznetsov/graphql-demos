package com.demo.graphql;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderFactory;
import org.dataloader.DataLoaderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.demo.graphql.model.Book;
import com.demo.graphql.model.Comment;
import com.demo.graphql.model.FindBookRequest;
import com.demo.graphql.model.MockData;
import com.fasterxml.jackson.databind.ObjectMapper;

import graphql.ExecutionInput;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Component
public class GraphQLProvider {
	private ObjectMapper objectMapper = new ObjectMapper();
	private GraphQL graphQL;
	private DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();

	@Bean
	public GraphQL graphQL() {
		return graphQL;
	}

	@Bean
	public DataLoaderRegistry dataLoaderRegistry() {
		return dataLoaderRegistry;
	}

	@PostConstruct
	public void init() {
		SchemaParser schemaParser = new SchemaParser();
		SchemaGenerator schemaGenerator = new SchemaGenerator();
		TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();

		//Multiple schema files are supported
		typeRegistry.merge(schemaParser.parse(getClass().getResourceAsStream("/types.graphqls")));
		typeRegistry.merge(schemaParser.parse(getClass().getResourceAsStream("/query.graphqls")));

		GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, buildWiring());
	}

	private RuntimeWiring buildWiring() {
		dataLoaderRegistry.register("comments", DataLoaderFactory.newDataLoader(commentsBatchLoader()));

		//data fetchers must match schema definition!
		return RuntimeWiring.newRuntimeWiring()
				.type(newTypeWiring("Query")
						.dataFetcher("bookById", getBookByIdDataFetcher())
						.dataFetcher("books", findDataFetcher())
				)
				.type(newTypeWiring("Book")
						.dataFetcher("author", getAuthorDataFetcher())
						.dataFetcher("comments", getCommentsDataFetcher())
				)
				.build();
	}

	public DataFetcher getBookByIdDataFetcher() {
		return dataFetchingEnvironment -> {
			//node arguments must be retrieved manually from the context
			String bookId = dataFetchingEnvironment.getArgument("id");
			return MockData.findBookById(bookId);
		};
	}

	//other data fetches are skipped

	public DataFetcher findDataFetcher() {
		return dataFetchingEnvironment -> {
			//input objects are passed as Maps, we have to manually convert them to DTO class
			Map argument = dataFetchingEnvironment.getArgument("request");
			FindBookRequest request = objectMapper.convertValue(argument, FindBookRequest.class);
			return MockData.findBooks(request);
		};
	}

	public DataFetcher getAuthorDataFetcher() {
		return dataFetchingEnvironment -> {
			Book book = dataFetchingEnvironment.getSource();
			return MockData.findAuthorById(book.getAuthorId());
		};
	}

	private DataFetcher getCommentsDataFetcher() {
		return dataFetchingEnvironment -> {
			Book book = dataFetchingEnvironment.getSource();
			DataLoader dataLoader = dataFetchingEnvironment.getDataLoader("comments");
			return dataLoader.load(book.getId());
		};
	}

	public BatchLoader commentsBatchLoader() {
		return (BatchLoader<String,List<Comment>>) keys -> {
			return CompletableFuture.supplyAsync(() -> MockData.getBookComments(keys));
		};
	}

}
