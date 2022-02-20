package com.example.bookdemo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginationParameters {
	Integer limit;
	String cursor;
	boolean reversed;
}
