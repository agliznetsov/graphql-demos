package com.example.bookdemo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;

@GraphQlTest(controllers = {QueryApiImpl.class, BookApiImpl.class})
public class ApiTests {

	@Autowired
	private GraphQlTester graphQlTester;

	@BeforeEach
	void setup() {
	}

	@Test
	void decodedResponse() {
		String query = "{\n"
				+ "  bookById(id: 1) {\n"
				+ "    name\n"
				+ "    author {\n"
				+ "      lastName\n"
				+ "    }\n"
				+ "  }\n"
				+ "}";
		this.graphQlTester.query(query)
				.execute()
				.path("bookById")
				.matchesJson("{ \"name\": \"Harry Potter and the Philosopher's Stone\", \"author\": { \"lastName\": \"Rowling\"  }}");
	}

}
