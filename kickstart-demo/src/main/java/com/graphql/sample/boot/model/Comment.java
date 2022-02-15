package com.graphql.sample.boot.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Comment {
	String text;
}
