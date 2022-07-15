package com.demo.graphql;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderFactory;
import org.dataloader.DataLoaderRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.demo.graphql.model.Book;
import com.demo.graphql.model.Comment;
import com.demo.graphql.model.FindBookRequest;
import com.demo.graphql.model.MockData;
import com.fasterxml.jackson.databind.ObjectMapper;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Configuration
public class GraphQLConfiguration {
	private ObjectMapper objectMapper = new ObjectMapper();

	@Bean
	public GraphQL graphQL(GraphQLSchema graphQLSchema) {
		return GraphQL.newGraphQL(graphQLSchema).build();
	}

	@Bean
	public DataLoaderRegistry dataLoaderRegistry() {
		return new DataLoaderRegistry();
	}

	@Bean
	public GraphQLSchema graphQLSchema(DataLoaderRegistry dataLoaderRegistry) {
		SchemaParser schemaParser = new SchemaParser();
		SchemaGenerator schemaGenerator = new SchemaGenerator();
		TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();

		//Multiple schema files are supported
		typeRegistry.merge(schemaParser.parse(getClass().getResourceAsStream("/types.graphqls")));
		typeRegistry.merge(schemaParser.parse(getClass().getResourceAsStream("/query.graphqls")));

		return schemaGenerator.makeExecutableSchema(typeRegistry, buildWiring(dataLoaderRegistry));
	}

	private RuntimeWiring buildWiring(DataLoaderRegistry dataLoaderRegistry) {
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
