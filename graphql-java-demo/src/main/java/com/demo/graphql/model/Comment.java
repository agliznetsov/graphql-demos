package com.demo.graphql.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Comment {
	String text;
}
